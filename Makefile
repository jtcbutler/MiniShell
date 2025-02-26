SOURCE=src
BUILD=build
TARGET=MiniShell.java
LIB=external/jansi-2.4.0.jar

default:
	javac -cp .:$(LIB):$(SOURCE) $(SOURCE)/$(TARGET) -d $(BUILD)

strict:
	javac -Xdoclint -Xlint -cp .:$(LIB):$(SOURCE) $(SOURCE)/$(TARGET) -d $(BUILD)

run: default
	java -cp .:$(LIB):$(BUILD) MiniShell
