#!/bin/bash

dockerComposeFile="docker-compose-${WHAT_TO_TEST}.yml"
#docker-compose -f $dockerComposeFile kill
#docker-compose -f $dockerComposeFile rm -f
docker-compose -f $dockerComposeFile build

# First boot up Zipkin Web and all of it's dependencies
docker-compose -f $dockerComposeFile up -d mysql web collector query

# Wait for the Zipkin apps to boot up
url="192.168.99.100"
waitTime=5
retries=48
totalWaitingTime=240
n=0
success=false

echo "Waiting for the apps to boot for [$totalWaitingTime] seconds"
until [ $n -ge $retries ]
do
  echo "Pinging Zipkin applications if they're alive..."
  nc -v -z -w 1 $url 9410 && success=true && break
  n=$[$n+1]
  echo "Failed... will try again in [$waitTime] seconds"
  sleep $waitTime
done

# Then the rest
docker-compose -f $dockerComposeFile up -d
