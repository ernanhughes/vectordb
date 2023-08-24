package com.fodala.ant.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class MessageTask extends Task {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void execute() throws BuildException {
        System.out.println("CustomTask executed with message: " + message);
        MainApp.sharedMessage = message;  // Communicate with the main Java application
    }
}
