#!/bin/bash

endpoint='http://localhost:8081/auth'

function accessToken {
  local accessToken=$1
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Access-Token: $accessToken" \
    -d '{ "query": "query { accessToken }"}' \
    $endpoint
}

function verifyPassword {
  local accessToken=$1
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Access-Token: $accessToken" \
    -d '{ "query": "query($input: EmailAuthInput) { verifyPassword(input: $input){ accessToken,authToken } }", "variables": { "input": { "emailAddress": "test@test.jp", "rawPassword": "rightPassword" } } }' \
  $endpoint
}

token=`accessToken $1 | jq -r '.data.accessToken'`
verifyPassword $token

