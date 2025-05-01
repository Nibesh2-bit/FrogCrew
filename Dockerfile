# Perform the build in the builder container
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /builder

# Copy the Maven wrapper and configuration
COPY FrogCrew_Back-End/mvnw .
COPY FrogCrew_Back-End/.mvn ./.mvn

# Copy project source
COPY FrogCrew_Back-End/src ./src
COPY FrogCrew_Back-End/pom.xml ./pom.xml

# Build the application
RUN chmod +x ./mvnw && ./mvnw package -DskipTests

# Find the generated JAR file
ARG JAR_FILE=target/FrogCrew_Back-End-0.0.1-SNAPSHOT.jar

# Perform the extraction in the same builder container
RUN java -Djarmode=tools -jar ${JAR_FILE} extract --layers --destination extracted

# This is the runtime container
FROM eclipse-temurin:21-jre
WORKDIR /application
# Copy the extracted jar contents from the builder container into the working directory in the runtime container
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/org/springframework/boot/loader/ ./org/springframework/boot/loader/
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
# Start the application using the extracted layers
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]