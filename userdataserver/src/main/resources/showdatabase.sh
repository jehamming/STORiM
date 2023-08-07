#!/bin/sh
exec java -cp .:lib/* com.hamming.storim.server.ShowDatabase *.db
