#!/bin/bash

if [ "$BRANCH" == "master" ] && [ "$JDK_VERSION" == "openjdk7" ] && [ "$PULL_REQUEST" == "false" ]; then
    echo "DEPLOY MASTER BUILD"
    echo "Current directory is $(pwd)"
    mvn clean deploy -DskipTests;
fi

if [ "$BRANCH" == "master" ] && [ "$JDK_VERSION" == "oraclejdk8" ] && [ "$PULL_REQUEST" == "false" ]; then
    echo "DEPLOY JDK8 lightblue-client-integration-test BUILD"
    cd lightblue-client-integration-test
    echo "Current directory is $(pwd)"
    mvn clean deploy -DskipTests;
    cd ..
fi
