#!/bin/bash

dockerComposeFile="docker-compose-${WHAT_TO_TEST}.yml"
docker-compose -f $dockerComposeFile kill
#docker-compose -f $dockerComposeFile rm -f
docker-compose -f $dockerComposeFile build
echo "Starting brewery apps..."
docker-compose -f $dockerComposeFile up -d
