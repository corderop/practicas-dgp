#!/bin/bash
if [ -v $1 ]
then

echo "No has indicado si test o demo"

else

HOST=dgp.esy.es
USER=u681824297.$1_ftp
PASSWD_FTP=dgp.7.host.FTP
PASSWD_SSH=dgp.7.host.a

if [ ! -v $2 ]
then
    sshpass -f <(printf '%s\n' $PASSWD_SSH) ssh -p 65002 u681824297@31.220.20.85 "cd domains/dgp.esy.es/public_html/$1/ ; rm -r *"
fi

ncftp -u $USER -p $PASSWD_FTP $HOST <<EOF
put -R server/*
EOF

exit 0

fi
