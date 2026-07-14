# cibseven-mcp-restapi Helm chart

Deploys the CIB seven "getting started" Spring Boot demo, which bundles the CIB
seven engine, the webapp and the **cibseven-mcp-restapi** MCP server into a
single container.

- **Image:** `cibseven/cibseven-mcp-restapi:0.0.3-SNAPSHOT`
  (built by the repository's `Dockerfile` and published via
  `.github/workflows/build-and-publish.yml`).
- **Template library:** all Kubernetes objects are produced by the shared CIB
  `common-tpl-lib` chart (`oci://harbor.cib.de/charts`, version `2.7.0`), the
  same library used by `cibseven-webclient/helm`. See
  [templates/objects.yaml](templates/objects.yaml).

## Install

```bash
# The common-tpl-lib dependency is vendored under charts/. If you need to
# refresh it:
#   helm dependency build

helm install my-mcp . \
  --namespace cibseven --create-namespace \
  --set adminUser.password="$(openssl rand -base64 24)"
```

## Configuration

| Value | Default | Description |
| --- | --- | --- |
| `adminUser.id` | `demo` | Demo admin user id. |
| `adminUser.password` | `change-me-please` | Admin password. Rendered into a **Secret**, not a ConfigMap. **Override this.** |
| `application.cibseven.mcp.process-mcp` | `false` | Enable the process-executor MCP server. |
| `application.cibseven.mcp.restapi-mcp` | `true` | Enable the REST API MCP server. |
| `application.cibseven.webclient.engineRest.url` | `http://localhost:8080` | Engine REST base URL used by the MCP server. |
| `global.image.repository` | `cibseven` | Image registry/organisation. |
| `image.tag` | `""` (→ `appVersion`) | Overrides the image tag. |
| `resources.enabled` / `global.resources.enabled` | `false` | Apply CPU/memory requests & limits. |

The full `application` map (this section of `values.yaml`) is rendered verbatim
into a Kubernetes Secret, mounted at `/opt/cib/conf/application.yaml`, and
layered on top of the image's baked-in config via
`SPRING_CONFIG_ADDITIONAL_LOCATION`. This keeps the admin password out of the
image and out of any ConfigMap.

### Security notes

- Admin credentials live in a Secret; the weak `demo/demo` default from the
  image is overridden. For real environments use an `ExternalSecret` / sealed
  secret rather than a plaintext `--set`.
- The container runs as a non-root user (`runAsUser: 1000`,
  `runAsNonRoot: true`), drops all Linux capabilities and disables privilege
  escalation.

> This demo bundles an in-memory H2 database and is intended for evaluation, not
> production. It runs as a single replica (`replicaCount: 1`).
