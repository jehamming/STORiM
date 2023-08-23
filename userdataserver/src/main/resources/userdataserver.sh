#!/bin/sh
exec java -cp .:lib/* com.hamming.userdataserver.STORIMUserDataServer > logfile 2>&1
