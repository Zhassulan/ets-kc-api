FROM openjdk:11.0.5-jdk
LABEL description="ETS API"
LABEL name="api-v1-ets"
ENV TZ Asia/Almaty
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]