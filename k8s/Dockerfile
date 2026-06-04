# Build the JAR first:
#   mvn clean package -DskipTests
#
# Then build this image from the project root:
#   docker build -f k8s/Dockerfile -t cibseven-license-demo:broken .
#
# For Docker Desktop Kubernetes: image is available immediately after build.
# For kind:  kind load docker-image cibseven-license-demo:broken
# For colima/k3s: docker save cibseven-license-demo:broken | colima ssh -- sudo ctr -n k8s.io images import -

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
