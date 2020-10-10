#!/bin/bash

HOST="dgp.esy.es"
USER="u681824297.$1_ftp"
PASSWD='dgp.7.host.FTP'

ncftp -u $USER -p $PASSWD $HOST <<EOF
rm -r *
put server/*
EOF

exit 0