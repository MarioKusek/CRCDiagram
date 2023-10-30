#!/bin/zsh

# use sdk to change JVM - this is needed if graal is used directly (without gradle)
#export SDKMAN_DIR=/Users/mario/.sdkman
#echo "before sourcing"
#[[ -s "/Users/mario/.sdkman/bin/sdkman-init.sh" ]] && source "/Users/mario/.sdkman/bin/sdkman-init.sh"
#sdk use java 20.1.0.r11-grl

./gradlew build fatJar
./gradlew nativeCompile
# native-image -jar build/libs/CrcDiagram-all-0.0.1-SNAPSHOT.jar crcDiagram
# make executable smaller by usin upx - can be installed by brew
#rm -rf ./build/graal/crcDiagram_small
#upx -7 ./build/graal/crcDiagram -o ./build/graal/crcDiagram_small

# install to my path
cp ./build/native/nativeCompile/crcDiagram /Users/mario/Applications/bin/crcDiagram
