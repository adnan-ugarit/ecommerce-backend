#!/bin/bash

declare project_dir=$(dirname $0)
declare dc_main=${project_dir}/docker-compose.yml

function start_all() {
    echo "Starting all services..."
    sudo docker-compose -f ${dc_main} up --build --force-recreate -d
    sudo docker-compose -f ${dc_main} logs -f
}

function stop_all() {
    sudo docker-compose -f ${dc_main} stop
    sudo docker-compose -f ${dc_main} rm -f
}

function start() {
    echo "Starting $1..."
    sudo docker-compose -f ${dc_main} up --build -d $1
    #sudo docker-compose -f ${dc_main} logs -f
}

function stop() {
    sudo docker-compose -f ${dc_main} stop $1
    sudo docker-compose -f ${dc_main} rm -f
}

action="start_all"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
