FROM openjdk:11.0.16

ARG LOCAL_APP_FILE=spring-1.jar

RUN mkdir /home/app

COPY target/${LOCAL_APP_FILE} /home/app/app.jar

WORKDIR /home/app

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar /home/app/app.jar --spring.config.location=$SPRING_CONFIG