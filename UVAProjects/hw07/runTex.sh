#!/usr/bin/env bash

# pdflatex */*.tex
pdflatex BlackBeard/BlackBeard.tex
# pdflatex *.tex
rm *.log
rm *.aux
