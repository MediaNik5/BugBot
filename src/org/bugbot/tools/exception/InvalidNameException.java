package org.bugbot.tools.exception;


public class InvalidNameException extends IllegalArgumentException {

    String name;

    public String getNameOfObject() {
        return name;
    }

    public InvalidNameException(String arg) {
        super();
        name = arg;
    }


}
