#!/usr/bin/env bash

for file in test/input/*.txt; do
    echo ${file}
    fname="$(basename ${file})"
    java CuttingCheese < ${file} > test/output/output_${fname}
    diff test/output/output_${fname} test/correct/check_${fname}
done
echo "Done."