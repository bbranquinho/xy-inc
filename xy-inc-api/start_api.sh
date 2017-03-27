#!/bin/bash
cd $1
gradle clean build
java -jar $2