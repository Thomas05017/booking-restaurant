#!/bin/bash
mkdir -p secrets

if [ ! -f secrets/mysql_root_password.txt ]; then
    openssl rand -base64 32 > secrets/mysql_root_password.txt
    echo "Password root MySQL generata"
fi

if [ ! -f secrets/mysql_password.txt ]; then
    openssl rand -base64 32 > secrets/mysql_password.txt
    echo "Password utente MySQL generata"
fi

echo "Setup completato!"
echo "Password root: $(cat secrets/mysql_root_password.txt)"
echo "Password user: $(cat secrets/mysql_password.txt)"