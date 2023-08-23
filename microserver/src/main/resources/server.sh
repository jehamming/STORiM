#!/bin/sh
exec java -cp .:lib/* com.hamming.storim.server.STORIMMicroServer > logfile 2>&1
