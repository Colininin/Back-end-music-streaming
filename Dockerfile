FROM gradle:8.10-jdk17
WORKDIR /opt/app
COPY ./build/libs/beeple-0.0.3-SNAPSHOT.jar ./
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar beeple-0.0.3-SNAPSHOT.jar"]