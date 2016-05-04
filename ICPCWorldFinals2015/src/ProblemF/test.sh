#!/usr/bin/env bash

javac Keyboarding.java
START=$(date +%s)
# unzip all of the test cases from ICPC and rename to be testCases
for file in ../testCases/keyboard/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java Keyboarding < ${file} > ../testCases/keyboard/${fname}.out
    diff ../testCases/keyboard/${fname}.out ../testCases/keyboard/${fname}.ans
done
END=$(date +%s)
DIFF=$(( $END - $START ))
echo "Done. It took $DIFF seconds"