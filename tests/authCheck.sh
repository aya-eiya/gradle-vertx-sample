#!/bin/bash

endpoint='http://localhost:8081/auth'

function sessionID {
  local sessionID=$1
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Session-Id: $sessionID" \
    -d '{ "query": "query { sessionID }"}' \
    $endpoint
}

function verifyPassword {
  local sessionID=$1
  curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Session-Id: $sessionID" \
    -d '{ "query": "query($input: EmailAuthInput) { verifyPassword(input: $input){ sessionID,authToken } }", "variables": { "input": { "emailAddress": "test@test.jp", "rawPassword": "rightPassword" } } }' \
  $endpoint
}

token=`sessionID $1 | jq -r '.data.sessionID'`
verifyPassword $token

