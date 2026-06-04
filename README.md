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

License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).
