#Simple makefile
# Note: the lines containing commands must start with a tab not spaces.
# Compile source code by typing the command make compile
compile:
	javac *.java
# Run the Hello java main class by typing the command make run 
run:
	java SortExperiment
# Clean the compiled class files by typing the command make clean
clean:
	rm *.class
# Javadoc the files into the html folder named javadoc
javadoc:
	javadoc -d javadoc *.java -private
	chmod 644 javadoc/*.html
# Cleandoc removes javadoc files.
cleanjavadoc:
	rm -Rf javadoc