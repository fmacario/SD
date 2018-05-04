#!/bin/bash

echo "Build and Run 04 - Paddock"
mkdir bin
javac -d bin/ -classpath "org.json.jar" src/JSON/JSON.java src/SV_Paddock/*.java
java -cp bin:org.json.jar SV_Paddock.SV_Paddock