#!/bin/bash

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_JDK_VERSION" == "openjdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "DEPLOY MASTER TRAVIS BUILD"
    echo "Current directory is $(pwd)"
    mvn clean deploy -DskipTests;
fi

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "DEPLOY JDK8 lightblue-client-integration-test TRAVIS BUILD"
    cd lightblue-client-integration-test
    echo "Current directory is $(pwd)"
    mvn clean deploy -DskipTests;
    cd ..
fi
