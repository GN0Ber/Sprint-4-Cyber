# ================================
# 📦 STAGE 1: Build com Maven
# ================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src

# Cache de dependências
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Compila o projeto
COPY src ./src
RUN mvn -B -DskipTests package


# ================================
# 🚀 STAGE 2: Runtime (JRE)
# ================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o JAR do estágio de build
COPY --from=build /src/target/*-SNAPSHOT.jar /app/app.jar

# ---- Segurança: rodar como usuário NÃO-root ----
RUN groupadd -r app && useradd -r -g app app \
    && chown -R app:app /app
USER app

# Perfil e ajustes de JVM container-aware
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -Dfile.encoding=UTF-8"

# Porta da API
EXPOSE 8085

# (Opcional) HEALTHCHECK — requer curl/wget instalado; deixe off se não quiser inflar a imagem
# RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
# HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=5 \
#   CMD curl -fsS http://localhost:8085/actuator/health/readiness || exit 1

# Entrypoint da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
