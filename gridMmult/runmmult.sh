#!/bin/bash
#  UNICEN
#  Parallel and Distributed Computing
#  Matrix Multiplication.
## author: Marcelo Rodriguez
#  usage: mmult [task rec loop]

############################################

GRIDGAIN_LIBS=$GRIDGAIN_HOME

for FILE in $(find $GRIDGAIN_HOME -name "*.jar");do
        GRIDGAIN_LIBS=$GRIDGAIN_LIBS:$FILE
done
        export GRIDGAIN_LIBS

if [ $1 == "-c" ]; then
	javac -Xlint:unchecked -classpath $GRIDGAIN_HOME:$GRIDGAIN_LIBS:.  -d ./bin ./src/*.java
        java -Xms2500M -classpath $GRIDGAIN_HOME:$GRIDGAIN_LIBS:./bin:. Main $2 $3 $4
else
	java -Xms2500M -classpath $GRIDGAIN_HOME:$GRIDGAIN_LIBS:./bin:. Main $1 $2 $3
fi

