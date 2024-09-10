FROM eclipse-temurin:21-jdk
RUN mkdir /opt/app
COPY target/coin-exec.jar /opt/app
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/coin-exec.jar"]