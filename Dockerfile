FROM openjdk:8
VOLUME /tmp
MAINTAINER <viktors.oginskis@gmail.com>
ADD app.jar app.jar
ENV JAVA_OPTS="-Xms32m -Xmx128m"
ENV SECRET="abcdefghijk"
ENTRYPOINT exec java $JAVA_OPTS -Dplay.http.secret.key=$SECRET -jar /app.jar
