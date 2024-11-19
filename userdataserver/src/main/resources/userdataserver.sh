#!/bin/bash
#
BASE=/tmp
PID=$BASE/userdataserver.pid
LOG=$BASE/userdataserver.log
COMMAND="java -cp .:lib/* com.hamming.userdataserver.STORIMUserDataServer"
USR=user

status() {
    echo
    echo "==== Status"

    if [ -f $PID ]
    then
        echo
        echo "Pid file: $( cat $PID ) [$PID]"
        echo
        ps -ef grep $( cat $PID )
    else
        echo
        echo "No Pid file"
    fi
}

start() {
    if [ -f $PID ]
    then
        echo
        echo "Already started. PID: [$( cat $PID )]"
    else
        echo "==== Start"
        touch $PID
        nohup $COMMAND >>$LOG 2>&1 &
        ppid=$!
        sleep 1
        if ps --pid $ppid>>/dev/null
        then echo $ppid >$PID
             echo "Done.[$ppid] "
             echo "$(date '+%Y-%m-%d %X'): START" >>$LOG
        else echo "Error... "
             /bin/rm $PID
        fi
    fi
}

stop() {
    echo "==== Stop"

    if [ -f $PID ]
    then
        if kill $( cat $PID )
        then echo "Stopped PID $( cat $PID )"
             echo "$(date '+%Y-%m-%d %X'): STOP" >>$LOG
        fi
        /bin/rm $PID
    else
        echo "No pid file. Already stopped?"
    fi
}

case "$1" in
    'start')
            start
            ;;
    'stop')
            stop
            ;;
    'restart')
            stop ; echo "Sleeping..."; sleep 1 ;
            start
            ;;
    'status')
            status
            ;;
    *)
            echo
            echo "Usage: $0 { start | stop | restart | status }"
            echo
            exit 1
            ;;
esac

exit 0

