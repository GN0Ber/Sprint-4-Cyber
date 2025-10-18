# ================================
# 📦 STAGE 1: Build com Maven
# ================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src

# Copia arquivos de config e baixa dependências (cache otimizado)
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Copia o restante do código e compila
COPY src ./src
RUN mvn -B -DskipTests package

# ================================
# 🚀 STAGE 2: Runtime com JRE enxuto
# ================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia o JAR gerado no estágio de build
COPY --from=build /src/target/*-SNAPSHOT.jar app.jar

# Porta padrão da API
EXPOSE 8085

# Carregar variáveis de ambiente do Docker Compose (.env)
ENV SPRING_PROFILES_ACTIVE=prod

# Iniciar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
