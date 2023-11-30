#!/bin/bash
echo Starting Java application with JAVA MAX RAM PERCENTAGE set to ${JAVA_MAX_RAM_PCENT}
java -XX:MaxRAMPercentage="${JAVA_MAX_RAM_PCENT}" -jar "/app/app.jar"