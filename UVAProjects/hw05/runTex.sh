#!/usr/bin/env bash

# pdflatex */*.tex
pdflatex FerryLoading/ferryLoading.tex
# pdflatex *.tex
rm *.log
rm *.aux
