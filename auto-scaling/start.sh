#!/bin/bash

declare project_dir=$(dirname $0)

echo $2 | sudo -S docker build -t $1 ${project_dir}/$1
sudo docker run --net ecommercebackend_default -d $1
