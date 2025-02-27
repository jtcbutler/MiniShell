SOURCE=src
BUILD=build
LIB=external/jansi-2.4.0.jar

default:
	javac -g -cp .:$(LIB):$(SOURCE) $(SOURCE)/MiniShell.java -d $(BUILD)

strict:
	javac -Xdoclint -Xlint -g -cp .:$(LIB):$(SOURCE) $(SOURCE)/MiniShell.java -d $(BUILD)

debug: default
	# to connect, run "jdb -attach localhost:8000" while in the src/ directory
	java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y -cp .:$(LIB):$(BUILD) MiniShell

run: default
	java -cp .:$(LIB):$(BUILD) MiniShell
