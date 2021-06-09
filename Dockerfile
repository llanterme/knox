FROM amazoncorretto:11

ARG JAR_FILE
ARG ARTIFACT_ID

EXPOSE 8080

#ADD ./target/${JAR_FILE} app.jar
ADD ./target/knox-service-1.0.0.jar app.jar

RUN sh -c 'touch /app.jar'

ENV JAVA_OPTS=""

# Regarding settings of java.security.egd, see http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]