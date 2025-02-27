SOURCE=src
BUILD=build
TARGET=MiniShell.java
LIB=external/jansi-2.4.0.jar

default:
	javac -cp .:$(LIB):$(SOURCE) $(SOURCE)/$(TARGET) -d $(BUILD)

strict:
	javac -Xdoclint -Xlint -cp .:$(LIB):$(SOURCE) $(SOURCE)/$(TARGET) -d $(BUILD)

debug: default
	# to connect, run "jdb -attach localhost:8000" while in the src/ directory
	java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y -cp .:$(LIB):$(BUILD) MiniShell

run: default
	java -cp .:$(LIB):$(BUILD) MiniShell
