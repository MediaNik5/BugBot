package org.bugbot.tools;

import org.bugbot.BugBot;

public class Response {


    public Response[] response;
    String datetime,
            filename,
            id,
            romtype,
            url,
            version;
    int size;

    public String toString() {
        return version + " " + filename + " " + romtype
                + "\n\n" + url
                + " (" + BugBot.getSizeOfBytes(size) + ")";
    }
}
