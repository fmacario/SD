#!/bin/bash

echo "Build and Run 06 - Stable"
mkdir bin
javac -d bin/ -classpath "org.json.jar" src/JSON/JSON.java src/SV_Stable/*.java
java -cp bin:org.json.jar SV_Stable.SV_Stable