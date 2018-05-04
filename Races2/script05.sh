#!/bin/bash

echo "Build and Run 04 - RacingTrack"
mkdir bin
javac -d bin/ -classpath "org.json.jar" src/JSON/JSON.java src/SV_RacingTrack/*.java
java -cp bin:org.json.jar SV_RacingTrack.SV_RacingTrack