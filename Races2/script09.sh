#!/bin/bash

echo "Build and Run 09 - Horse"
mkdir bin
javac -d bin/ -cp "org.json.jar" src/JSON/JSON.java src/CL_Horses/*.java
java -cp bin:org.json.jar CL_Horses.MainHorse