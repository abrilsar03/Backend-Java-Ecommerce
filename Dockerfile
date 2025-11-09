FROM openjdk:21-rc-jdk-slim AS build

RUN apt-get update && apt-get install -y --no-install-recommends bash ca-certificates \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /src

COPY gradlew ./
COPY gradle gradle
COPY settings.gradle* ./
COPY build.gradle* ./
RUN chmod +x ./gradlew

RUN ./gradlew --no-daemon dependencies || true

COPY . .

ARG GRADLE_TASK=build
RUN ./gradlew --no-daemon clean ${GRADLE_TASK} -x test


FROM openjdk:21-rc-jdk-slim AS runtime


RUN useradd -r -u 10001 appuser

WORKDIR /app

ARG JAR_FILE_PATTERN="build/libs/*.jar"
COPY --from=build /src/${JAR_FILE_PATTERN} /app/app.jar

ENV JAVA_OPTS=""

EXPOSE 3000

USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]