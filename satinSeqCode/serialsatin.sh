#!/bin/bash
#  UNICEN
#  Parallel and Distributed Computing
#  Matrix Multiplication.
# author: Marcelo Rodriguez
# $1 = rec
# $2 = loop
#usage sh serialsatin.sh rec loop

if [ $1 == "-c" ]; then
         javac -Xlint:unchecked -classpath $GRIDGAIN_HOME:$GRIDGAIN_LIBS:.  -d ./bin ./src/*.java
	 java -Xmx2500M -classpath ./bin:. Mmult $2 $3 
else
  	java -Xmx2500M -classpath ./bin:.  Mmult $1 $2
fi
