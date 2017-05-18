compute: 
	pdflatex memoire.tex
	makeglossaries memoire.glo
	bibtex memoire
	pdflatex memoire.tex
	pdflatex memoire.tex
	clear

clean: 
	rm *.aux *.log *.out *.toc *.glg *.glo *.gls *.ist
