#!/bin/bash

endpoint='http://localhost:8081/auth'

function notExistQuery {
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -d '{ "query": "query { notExist }"}' \
    $endpoint
}

notExistQuery

