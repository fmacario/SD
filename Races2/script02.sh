#!/bin/bash

echo "Build and Run 02 - ControlCentre"
mkdir bin
javac -d bin/ -classpath "org.json.jar" src/JSON/JSON.java src/SV_ControlCentre/*.java
java -cp bin:org.json.jar SV_ControlCentre.SV_ControlCentre