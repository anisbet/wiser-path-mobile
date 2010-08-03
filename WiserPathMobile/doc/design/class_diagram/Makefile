###########################
 # Makefile for making LaTeX
 # assignment /home/anisbet/Work/docs/design/class_diagram/report.
 # Date: Xxx nn, yyyy
 ###########################
 
DOC_1 = /home/anisbet/Work/docs/design/class_diagram/report

 
doc1:
	latex ${DOC_1}
	dvipdf ${DOC_1}
	evince ${DOC_1}.pdf&
 
clean:
	-rm ${DOC_1}.log
	-rm ${DOC_1}.aux
	-rm ${DOC_1}.dvi
	-rm *~
 
open:
	kate ${DOC_1}.tex &
 
word_count:
	wc -w ${DOC_1}.tex

