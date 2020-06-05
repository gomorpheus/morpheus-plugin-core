#!/usr/bin/env bash
# Script to build & publish all of the plugins locally.
#set -x # Debug
set -e # Stop on any failure
./gradlew morpheus-plugin-api:test morpheus-plugin-api:pTML

for plugin in $(ls -1 | grep -e "-plugin$")
do
	./gradlew "${plugin}":test "${plugin}":assemble
done
