#!/bin/bash

echo "Build and Run 01 - BettingCentre"
mkdir bin
javac -d bin/ -classpath "org.json.jar" src/JSON/JSON.java src/SV_BettingCentre/*.java
java -cp bin:org.json.jar SV_BettingCentre.SV_BettingCentre 