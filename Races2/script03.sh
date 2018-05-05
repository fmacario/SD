#!/bin/bash

echo "Build and Run 03 - GRI"
mkdir bin
javac -d bin/ -classpath "genclass.jar:org.json.jar" src/JSON/JSON.java src/Enum/*.java src/SV_GRI/*.java
java -cp bin:org.json.jar:genclass.jar SV_GRI.SV_GRI 