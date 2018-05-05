#!/bin/bash

echo "Build and Run 10 - Spectator"
mkdir bin
javac -d bin/ -cp "org.json.jar" src/JSON/JSON.java src/CL_Spectators/*.java
java -cp bin:org.json.jar CL_Spectators.MainSpectator