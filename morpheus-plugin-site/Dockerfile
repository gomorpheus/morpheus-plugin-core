FROM eclipse-temurin:17.0.9_9-jdk-alpine

MAINTAINER Morpheus <docker@morpheusdata.com>

RUN mkdir -p /app
RUN mkdir -p /config
RUN mkdir -p /morpheus/logs
ADD entrypoint.sh /entrypoint.sh
RUN chmod 755 /entrypoint.sh
ADD build/libs/*.jar /app/app.jar

EXPOSE 8090

ENTRYPOINT ["/entrypoint.sh"]

CMD ["java"]
