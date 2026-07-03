#!/bin/bash -ex

VERSION=${VERSION:-$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)}

if [ -z "$JAVA" ]; then
  JAVA_ARGUMENT=""
else
  JAVA_ARGUMENT="--build-arg JAVA=${JAVA}"
fi

IMAGE=cibseven/cibseven-mcp-restapi

docker buildx build .                         \
    -t "${IMAGE}:${VERSION}"                  \
    --platform linux/amd64                    \
    ${JAVA_ARGUMENT}                          \
    --cache-to type=gha,scope="$GITHUB_REF_NAME-java${JAVA:-21}-image" \
    --cache-from type=gha,scope="$GITHUB_REF_NAME-java${JAVA:-21}-image" \
    --load

docker inspect "${IMAGE}:${VERSION}"  | grep "Architecture" -A2
