#!/bin/bash
echo Starting Java application with JAVA MAX RAM PERCENTAGE set to ${JAVA_MAX_RAM_PCENT}
java -XX:MaxRAMPercentage="${JAVA_MAX_RAM_PCENT}" -Dmicronaut.config.files="/config/application.yml" -Dlogback.configurationFile="/config/logback.xml" -jar "/app/app.jar"