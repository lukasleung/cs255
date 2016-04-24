#!/usr/bin/env bash

# pdflatex */*.tex
# pdflatex BlackBeard/BlackBeard.tex
pdflatex TA_Scheduling.tex
# pdflatex *.tex
rm *.log
rm *.aux
