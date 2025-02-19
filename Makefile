SOURCE=src
BUILD=build
LIB=external/jansi-2.4.0.jar

compile:
	javac -g -cp .:$(LIB):$(SOURCE) $(SOURCE)/MiniShell.java -d $(BUILD)

debug: compile
	# to connect, run "jdb -attach localhost:8000" while in the src/ directory
	java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y -cp .:$(LIB):$(BUILD) MiniShell

run: compile
	java -cp .:$(LIB):$(BUILD) MiniShell
