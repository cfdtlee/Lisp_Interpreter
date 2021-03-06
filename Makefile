#
# A simple makefile for compiling three java classes
#

# define a makefile variable for the java compiler
#
JCC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#
JFLAGS = -g

# typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
#
default: Interpreter.class Sexp.class Lexical.class Parse.class Printer.class Evaluator.class Typechecker.class

# this target entry builds the Average class
# the Average.class file is dependent on the Average.java file
# and the rule associated with this entry gives the command to create it
#
Interpreter.class: Interpreter.java
		$(JCC) $(JFLAGS) Interpreter.java

Typechecker.class: Typechecker.java
		$(JCC) $(JFLAGS) Typechecker.java

Sexp.class: Sexp.java
		$(JCC) $(JFLAGS) Sexp.java

Lexical.class: Lexical.java
		$(JCC) $(JFLAGS) Lexical.java

Parse.class: Parse.java
		$(JCC) $(JFLAGS) Parse.java

Printer.class: Printer.java
		$(JCC) $(JFLAGS) Printer.java

Evaluator.class: Evaluator.java
		$(JCC) $(JFLAGS) Evaluator.java
# To start over from scratch, type 'make clean'.  
# Removes all .class files, so that the next make rebuilds them
#
clean: 
		$(RM) *.class