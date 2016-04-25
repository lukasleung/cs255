#!/usr/bin/env bash

printf "\n\n\nDecompress:(From Correct)\n___________________________________\n"
printf "case 1:\n\n"
time java Huffman + < compressed/abra_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/abra_uncompressed.txt
printf "case 2:\n\n"
time java Huffman + < compressed/bible_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/bible_uncompressed.txt
printf "case 3:\n\n"
time java Huffman + < compressed/chromosome11-human_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/moby1_uncompressed.txt
printf "case 4:\n\n"
time java Huffman + < compressed/moby1_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/moby1_uncompressed.txt
printf "case 5:\n\n"
time java Huffman + < compressed/muchado_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/muchado_uncompressed.txt
printf "case 6:\n\n"
time java Huffman + < compressed/pi-1million_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/pi-1million_uncompressed.txt
printf "case 7:\n\n"
time java Huffman + < compressed/pi-10million_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/pi-10million_uncompressed.txt
printf "case 8:\n\n"
time java Huffman + < compressed/sedgewick-algc_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/sedgewick-algc_uncompressed.txt
printf "case 9:\n\n"
time java Huffman + < compressed/world192_compressed.txt | java MoveToFront + | java BurrowsWheeler + > testing/uncompressed/world192_uncompressed.txt
