#!/bin/bash

realm_file="./keycloak/realm.json"
env_file=".env"
docker_compose_file="docker-compose.yml"

echo "Enter the server ip (local network):"
read ip
sed -i "s/{serverIp}/$ip/g" "$realm_file"
echo "NUXT_PUBLIC_SERVER_IP=$ip" >> "$env_file"

echo "Enter database username:"
read dbUser
sed -i "s/DB_USER/$dbUser/g" "$docker_compose_file"
echo "DB_USER=$dbUser" >> "$env_file"

echo "Enter database password:"
read dbPassword
sed -i "s/DB_PASSWORD/$dbPassword/g" "$docker_compose_file"
echo "DB_PASSWORD=$dbPassword" >> "$env_file"

echo "DB_HOST=db" >> "$env_file"

echo "Enter keycloak username:"
read keycloakUser

echo "Enter keycloak password:"
read keycloakPassword

echo "Starting file server up!"

docker compose up -d
docker run -d --name keycloak -p 8080:8080 -e KEYCLOAK_ADMIN=$keycloakUser -e KEYCLOAK_ADMIN_PASSWORD=$keycloakPassword -v ./keycloak/realm.json:/opt/keycloak/data/import/realm.json quay.io/keycloak/keycloak:latest start-dev --import-realm