#!/bin/bash

echo "Loading configuration for http://localhost:$1..."
curl -X POST http://localhost:$1/actuator/refresh
