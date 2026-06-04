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

### Why this works

`CIBSEVEN_WEBCLIENT_AUTHENTICATION_JWTSECRET` is the only mechanism that reaches both consumers:

| Consumer | Without env var | With env var |
|---|---|---|
| Webclient (Spring) | reads `application.yaml` → `2tURHZ7...` | Spring relaxed binding → `RtURHZ7...` ✅ |
| Engine (`Configuration.java`) | reads classpath `.properties` → `RtURHZ7...` | `System.getenv()` priority 1 → `RtURHZ7...` ✅ |

---

License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).
