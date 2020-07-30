#!/bin/zsh

# use sdk to change JVM
export SDKMAN_DIR=/Users/mario/.sdkman
echo "before sourcing"
[[ -s "/Users/mario/.sdkman/bin/sdkman-init.sh" ]] && source "/Users/mario/.sdkman/bin/sdkman-init.sh"
sdk use java 20.1.0.r11-grl

./gradlew build fatJar
native-image -jar build/libs/CrcDiagram-all-0.0.1-SNAPSHOT.jar crcDiagram
cp crcDiagram /Users/mario/Applications/bin/crcDiagram
