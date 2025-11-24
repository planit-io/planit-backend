# Dockerfile
FROM gradle:8.4-jdk21 AS builder
WORKDIR /workspace
COPY . /workspace

# costruisci l'app Quarkus in modalità uber-jar
RUN ./gradlew build -x test

FROM amazoncorretto:21-alpine-jdk AS runtime
WORKDIR /app

# copia l'app Quarkus generata
COPY --from=builder /workspace/build/quarkus-app /app

ENV JAVA_OPTIONS="-Xms256m -Xmx512m"
EXPOSE 8080

# usare utente non-root
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

ENTRYPOINT ["sh","-c","java $JAVA_OPTIONS -jar /app/*.jar"]
