#!/bin/bash

# This script is used to start the Morpheus Plugin Site server

# Java 17 or higher is required
# Get the Java version
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
# Extract the major version number
major_version=$(echo "$java_version" | awk -F. '{print $1}')
# Compare the major version number
if [ "$major_version" -lt 17 ]; then
    echo "Java version is 17 or less. Please upgrade to Java 17 or higher."
    exit 1
else
	echo "Java version is $java_version"
fi

# Get the name of the current working directory
current_dir=$(basename "$PWD")
# Check if the current directory is not 'morpheus-plugin-site'
if [ "$current_dir" != "morpheus-plugin-site" ]; then
    # Change to the 'morpheus-plugin-site' directory
    cd morpheus-plugin-site || { echo "Failed to change directory to 'morpheus-plugin-site'"; exit 1; }
fi


# go to the project root
cd ..
# Build the project
./gradlew morpheus-plugin-site:shadowJar
# find the jar file to run
file=$(ls morpheus-plugin-site/build/libs/morpheus-plugin-site-*-all.jar 2>/dev/null | head -n 1)
if [ -n "$file" ]; then
    java -jar "$file"
else
    echo "Could not find jar in build directory 'morpheus-plugin-site/build/libs/'."
fi
