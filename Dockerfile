# Perform the build in the builder container
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /builder
# Copy the Maven or Gradle wrapper and configuration
COPY mvnw .
COPY .mvn ./.mvn
# For Maven
# COPY build.gradle . # For Gradle
# COPY gradle . # For Gradle

# Copy project source
COPY FrogCrew_Back-End/src ./FrogCrew_Back-End/src
COPY FrogCrew_Back-End/pom.xml ./FrogCrew_Back-End/pom.xml
# For Maven
# COPY FrogCrew_Back-End/build.gradle ./FrogCrew_Back-End/build.gradle # For Gradle

# Build the application (adjust command based on your build tool)
RUN cd FrogCrew_Back-End && chmod +x ./mvnw && ./mvnw package -DskipTests # For Maven
# RUN cd FrogCrew_Back-End && ./gradlew build -x test # For Gradle

# Find the generated JAR file
ARG JAR_FILE=FrogCrew_Back-End/target/*.jar

# Perform the extraction in the same builder container
RUN cd FrogCrew_Back-End && java -Djarmode=tools -jar ${JAR_FILE} extract --layers --destination extracted

# This is the runtime container
FROM eclipse-temurin:21-jre
WORKDIR /application
# Copy the extracted jar contents from the builder container into the working directory in the runtime container
COPY --from=builder /builder/FrogCrew_Back-End/extracted/dependencies/ ./
COPY --from=builder /builder/FrogCrew_Back-End/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/FrogCrew_Back-End/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/FrogCrew_Back-End/extracted/application/ ./
# Start the application jar
ENTRYPOINT ["java", "-jar", "application.jar"]