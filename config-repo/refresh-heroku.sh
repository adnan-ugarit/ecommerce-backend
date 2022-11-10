#!/bin/bash

echo "Loading configuration for https://adnan-$1.herokuapp.com..."
curl -X POST https://adnan-$1.herokuapp.com/actuator/refresh
