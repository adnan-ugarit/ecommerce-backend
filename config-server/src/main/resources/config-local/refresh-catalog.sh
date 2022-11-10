#!/bin/bash

echo "Loading configuration for catalog-service..."
curl -X POST http://localhost:8080/actuator/refresh
