#!/usr/bin/env bash

# pdflatex */*.tex
pdflatex fullReport.tex
# pdflatex *.tex
rm *.log
rm *.aux
