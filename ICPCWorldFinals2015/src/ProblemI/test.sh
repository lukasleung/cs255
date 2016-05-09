#!/usr/bin/env bash

javac ShipTraffic.java
START=$(date +%s)
# unzip all of the test cases from ICPC and rename to be testCases
for file in ../testCases/ship/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java ShipTraffic < ${file} > ../testCases/ship/${fname}.out
    diff ../testCases/ship/${fname}.out ../testCases/ship/${fname}.ans
done
END=$(date +%s)
DIFF=$(( $END - $START ))
echo "Done. It took $DIFF seconds"