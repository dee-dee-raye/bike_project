#!/bin/bash

docker-compose up -d
sleep 10
docker cp bike.sql postgres_container:/bike.sql
docker exec postgres_container psql -f bike.sql -U postgres