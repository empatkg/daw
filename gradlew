#!/usr/bin/env sh

# Gradle start up script for UN*X

# Add default JVM options
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"

# Use the gradle wrapper jar
exec java $DEFAULT_JVM_OPTS $JAVA_OPTS -jar "$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar" "$@"