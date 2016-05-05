#!/usr/bin/env bash

javac Catering.java
START=$(date +%s)
# unzip all of the test cases from ICPC and rename to be testCases
for file in ../testCases/catering/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java Catering < ${file} > ../testCases/catering/${fname}.out
    diff ../testCases/catering/${fname}.out ../testCases/catering/${fname}.ans
done
END=$(date +%s)
DIFF=$(( $END - $START ))
echo "Done. It took $DIFF seconds"
# clean
rm *.class