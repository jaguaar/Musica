FROM bellsoft/liberica-openjdk-alpine-musl:17

VOLUME /tmp

COPY libs/main-1.3.98-original.jar libs/main-1.3.98-original.jar

ADD build/libs/Musica-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=prod -jar /app.jar

