#!/bin/bash

# Gradle wrapper script
cd "$(dirname "$0")"

# Extract gradle version from properties
GRADLE_VERSION=$(grep 'distributionUrl' gradle/wrapper/gradle-wrapper.properties | sed 's/.*gradle-\(.*\)-bin.zip.*/gradle-\1/')

# Set up environment
GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
WRAPPER_DIR="$GRADLE_USER_HOME/wrapper/dists"

# Find or download gradle
DISTS=$(find "$WRAPPER_DIR" -maxdepth 1 -name "${GRADLE_VERSION#gradle}-*" -type d 2>/dev/null | head -1)
if [ -z "$DISTS" ] || [ ! -f "$DISTS/bin/gradle" ]; then
    mkdir -p "$WRAPPER_DIR"
    cd "$WRAPPER_DIR"
    URL="https://services.gradle.org/distributions/${GRADLE_VERSION}.zip"
    curl -sL "$URL" -o gradle.zip
    unzip -q gradle.zip
    rm gradle.zip
    cd - > /dev/null
    DISTS=$(find "$WRAPPER_DIR" -maxdepth 1 -name "${GRADLE_VERSION#gradle}-*" -type d | head -1)
fi

exec "$DISTS/bin/gradle" "$@"