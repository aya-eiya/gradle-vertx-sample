#!/bin/bash

accessToken=$1

curl -X POST \
  -H "Content-Type: application/json" \
  -H "Access-Token: testToken_1" \
  -d ' { "query": "query($input: EmailAuthInput) { verifyPassword(input: $input){ token } }", "variables": { "input": { "emailAddress": "test@test.jp", "rawPassword": "rightPassword" } } }' \
'http://localhost:8081/auth'

