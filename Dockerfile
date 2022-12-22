FROM ubuntu:latest

VOLUME /tmp

ADD build/libs/Musica-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && \
    apt-get install -y libstdc++6 && \
    apt-get install -y openjdk-17-jre-headless


ENTRYPOINT ["java","-jar","/app.jar"]
