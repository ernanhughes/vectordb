package com.fodala.ant.task;

import com.fodala.Application;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTask extends Task {
    private static final Logger logger = LoggerFactory.getLogger(MessageTask.class);
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void execute() throws BuildException {
        logger.info("MessageTask executed with message: " + message);
        Application.messages.add(message);
    }
}
