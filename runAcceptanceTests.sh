#!/bin/bash
set -e

echo "Running acceptance tests with the following parameters [$@]"

docker-compose -f docker-compose-$WHAT_TO_TEST.yml run acceptance-tests ./gradlew test "$@" --stacktrace --info --no-daemon
