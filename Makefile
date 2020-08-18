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

Api.class: Api.java URLtoJSON.java
	$(JCC) $(JFLAGS) -cp ./lib/java-json.jar ./URLtoJSON.java Api.java

URLtoJSON.class: URLtoJSON.java
	$(JCC) $(JFLAGS) -cp ./lib/java-json.jar URLtoJSON.java

# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
#
clean:
	$(RM) *.class
