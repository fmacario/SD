#!/bin/bash

echo "Build and Run 07 - Broker"
mkdir bin
javac -d bin/ -cp "org.json.jar" src/JSON/JSON.java src/CL_Broker/*.java
java -cp bin:org.json.jar CL_Broker.MainBroker