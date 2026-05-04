#!/usr/bin/env sh

# Gradle wrapper that uses system gradle or downloads

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
WRAPPER_PROPERTIES="gradle/wrapper/gradle-wrapper.properties"

if [ -f "$WRAPPER_PROPERTIES" ]; then
    GRADLE_VERSION=$(grep 'distributionUrl' "$WRAPPER_PROPERTIES" | sed 's/.*gradle-\(.*\)-bin.zip.*/gradle-\1/')
else
    GRADLE_VERSION="gradle-8.7"
fi

DISTS="$GRADLE_USER_HOME/wrapper/dists"
EXTRACTED=$(find "$DISTS" -maxdepth 2 -name "${GRADLE_VERSION#gradle}*" -type d 2>/dev/null | head -1)

if [ -z "$EXTRACTED" ] || [ ! -f "$EXTRACTED/bin/gradle" ]; then
    mkdir -p "$DISTS"
    cd "$DISTS"
    URL="https://services.gradle.org/distributions/${GRADLE_VERSION}.zip"
    echo "Downloading $URL..."
    curl -sL "$URL" -o gradle.zip
    unzip -q gradle.zip
    rm -f gradle.zip
    EXTRACTED=$(find "$DISTS" -maxdepth 1 -name "${GRADLE_VERSION#gradle}*" -type d | head -1)
fi

exec "$EXTRACTED/bin/gradle" "$@"