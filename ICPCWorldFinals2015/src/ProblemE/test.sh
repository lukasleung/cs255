#!/usr/bin/env bash

javac Evolution.java
START=$(date +%s)
# unzip all of the test cases from ICPC and rename to be testCases
for file in ../testCases/evolution/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java Evolution < ${file} > ../testCases/evolution/${fname}.out
    diff -q ../testCases/evolution/${fname}.out ../testCases/evolution/${fname}.ans
done
END=$(date +%s)
DIFF=$(( $END - $START ))
echo "Done. It took $DIFF seconds"