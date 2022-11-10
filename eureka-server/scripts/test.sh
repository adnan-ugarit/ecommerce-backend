#!/bin/bash

cd ..
mvn -Dtest=$1 test
