# CIB seven – License Issue Reproduction (branch: `license-issue`)

This branch is based on the `main` branch of the CIB seven Spring Boot get-started project,
upgraded to **CIB seven 2.2.0-ee**.  
It reproduces the license decryption failure reported by customers and documents the fix.

---

## The Bug

When setting the license key via Admin UI, the following error appears:

```
ENGINE-REST-HTTP500: Failed to decrypt license signature.
The JWT secret may have been rotated.
```

### Root Cause

There are **two independent consumers** of `jwtSecret` that must hold the same value:

| Consumer | Reads from |
|---|---|
| **Webclient** (Admin UI / REST client) | Spring environment: `application.yaml`, env var (relaxed binding) |
| **Engine** reads `System.getenv(...)` first, then classpath `cibseven-webclient.properties` — **never Spring context** |

In this repo, the mismatch is:

- `application.yaml` sets `jwtSecret: 2tURHZ7...` → webclient uses this to sign JWT auth tokens sent to engine-rest
- `cibseven-webclient.properties` has `jwtSecret=RtURHZ7...` → engine uses this to verify JWT tokens and to decrypt the license signature
- Different keys → engine cannot reconcile the request → `Failed to decrypt license signature`

---

## How to Reproduce

```bash
mvn spring-boot:run
```

Open http://localhost:8080/webapp, log in as `demo`/`demo`,  
go to **Admin → System → License Key**, and paste any license key.

---

## Fix

### Remove `jwtSecret` from `application.yaml`, rely on the `.properties` file

In `application.yaml`:
1. Remove (or comment out) `cibseven.webclient.authentication.jwtSecret`
2. Uncomment `spring.config.import` so Spring also imports `cibseven-webclient.properties`

```yaml
spring:
  config:
    import:
      - optional:classpath:cibseven-webclient.properties

cibseven:
  webclient:
    authentication:
      # jwtSecret must NOT be set here — managed via cibseven-webclient.properties
```

Both sides now read the same value from `cibseven-webclient.properties`.

---

## Generate a New Secret

```bash
openssl rand -base64 128 | tr -d '\n'
```

---

## Kubernetes Reproduction (branch: `license-issue-k8s`)

The `k8s/` folder contains manifests that demonstrate the broken → fixed pattern
using a single Docker image. The env var is the only difference between deployments.

### Prerequisites

- Docker + local Kubernetes (Docker Desktop, colima `--kubernetes`, kind, minikube)
- `kubectl` configured to point at the local cluster

### Build the image

```bash
mvn clean package -DskipTests
docker build -f k8s/Dockerfile -t cibseven-license-demo:broken .
```

For **colima/k3s** — load image into containerd:
```bash
docker save cibseven-license-demo:broken | colima ssh -- sudo ctr -n k8s.io images import -
```

For **kind**:
```bash
kind load docker-image cibseven-license-demo:broken
```

### Reproduce the broken state

```bash
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/deployment-broken.yaml
kubectl rollout status deployment/cibseven-broken
```

Open http://localhost:30080/webapp, log in as `demo`/`demo`,  
go to **Admin → System → License Key**, paste any license key, and observe the error in logs:

```bash
kubectl logs -l app=cibseven --follow
# ENGINE-REST-HTTP500: Failed to decrypt license signature. The JWT secret may have been rotated.
```

### Apply the fix (same image, no rebuild)

```bash
kubectl delete deployment cibseven-broken
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment-fix.yaml
kubectl rollout status deployment/cibseven-fix
```

Open http://localhost:30080/webapp — setting the license key now succeeds.

### Common traps — why partial fixes still fail

**Trap 1: `jwtSecret` set only in a ConfigMap-mounted `application.yaml`**

```yaml
# ConfigMap → application.yaml
cibseven:
  webclient:
    authentication:
      jwtSecret: <new-value>   # Spring picks this up ✅
```

→ Webclient aligned, but `Configuration.java` never touches Spring context →
engine still reads the baked-in `cibseven-webclient.properties` → **still broken**.

---

**Trap 2: `cibseven-webclient.properties` mounted as a volume, but `spring.config.import` missing**

```yaml
# deployment.yaml
volumes:
  - name: props
    configMap:
      name: webclient-props
volumeMounts:
  - mountPath: /app/cibseven-webclient.properties
    name: props
    subPath: cibseven-webclient.properties
```

→ Engine reads the file (classpath lookup succeeds) → engine aligned ✅  
→ But `spring.config.import` is not in `application.yaml` → Spring ignores the file →
webclient still uses the `application.yaml` value → **still broken**.

---

**Trap 3: `cibseven-webclient.properties` mounted AND `spring.config.import` present**

This works — but only if the mount path is on the Spring classpath. If the mount
path is not on the classpath, `Configuration.java`'s classloader won't find it either.
Use the env var instead to avoid path resolution issues entirely.

---

**Trap 4: Secret updated, pod not restarted**

```bash
kubectl edit secret cibseven-secrets   # value changed
# pod keeps running — env vars are snapshotted at pod start
```

→ The running pod still has the old value in its environment.
Kubernetes does **not** restart pods when a Secret changes.

```bash
kubectl rollout restart deployment/cibseven-fix
```

---

**Trap 5: Rolling update — mixed-generation pods**

During a rolling update, old pods (with the baked-in `2tURHZ7...` in `application.yaml`)
and new pods (with the env var override) coexist. License-check requests that land on an
old pod fail. The error appears intermittently until the rollout completes.

Ensure `imagePullPolicy: Always` and use a versioned image tag (not `latest`) so both
generations are never live simultaneously for longer than the rollout window.

---

**Trap 6: Env var silently absent — typo in name or missing `secretKeyRef`**

If the env var name is misspelled (e.g. `CIBSEVEN_WEBCLIENT_AUTH_JWTSECRET` instead of
`CIBSEVEN_WEBCLIENT_AUTHENTICATION_JWTSECRET`), both sides silently fall back to their
baked-in values. No error is logged about the misconfiguration itself.

If `secretKeyRef.key` doesn't match the key in the Secret exactly, the pod either fails
to start (if `optional: false`) or the env var is injected as empty string.

Verify before deploying:
```bash
kubectl get secret cibseven-secrets -o jsonpath='{.data}' | jq 'keys'
kubectl set env deployment/cibseven-fix --list | grep JWT
```

---

**Trap 7: Secret not applied in the target namespace / environment**

The Secret exists in `dev` but was never applied to `staging` or `prod`. The deployment
spec references it, so either:
- the pod fails to start with `secret "cibseven-secrets" not found`, or
- if the volume/env is marked `optional: true`, the var is absent → mismatch returns.

```bash
kubectl get secret cibseven-secrets -n <namespace>
```

---

**Trap 8: External config server (Spring Cloud Config) rotates the secret**

If `application.yaml` is served from a Spring Cloud Config Server and someone updates
`jwtSecret` there, Spring Boot refreshes the value (especially with `@RefreshScope`).
The webclient picks up the new value, but `Configuration.java` on the engine side reads
`System.getenv()` — which still has the old Secret value until the pod is restarted.
The mismatch returns silently.

When using Spring Cloud Config, the K8s Secret and the config server entry for `jwtSecret`
must always be updated together, followed by a pod restart.

---

### Why the env var is the only safe lever — and how to set it

`CIBSEVEN_WEBCLIENT_AUTHENTICATION_JWTSECRET` is the only configuration mechanism that reaches both consumers simultaneously:

| Consumer | Without env var | With env var |
|---|---|---|
| Webclient (Spring) | reads `application.yaml` → `2tURHZ7...` | Spring relaxed binding → `RtURHZ7...` ✅ |
| Engine (`Configuration.java`) | reads classpath `.properties` → `RtURHZ7...` | `System.getenv()` priority 1 → `RtURHZ7...` ✅ |

**Kubernetes (recommended)** — store the secret in a K8s Secret and inject it as an env var:

```yaml
# secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: cibseven-secrets
type: Opaque
stringData:
  jwtSecret: "<generate: openssl rand -base64 128 | tr -d '\n'>"
```

```yaml
# deployment.yaml — env section
env:
  - name: CIBSEVEN_WEBCLIENT_AUTHENTICATION_JWTSECRET
    valueFrom:
      secretKeyRef:
        name: cibseven-secrets
        key: jwtSecret
```

**Docker Compose:**

```yaml
services:
  cibseven:
    environment:
      - CIBSEVEN_WEBCLIENT_AUTHENTICATION_JWTSECRET=<your-secret>
```

**Local / shell:**

```bash
export CIBSEVEN_WEBCLIENT_AUTHENTICATION_JWTSECRET=<your-secret>
mvn spring-boot:run
```

---

License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).
