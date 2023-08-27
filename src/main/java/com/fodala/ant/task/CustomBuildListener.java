package com.fodala.ant.task;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBuildListener implements BuildListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomBuildListener.class);

    @Override
    public void buildStarted(BuildEvent event) {
        logger.info("Build Started");
    }

    @Override
    public void buildFinished(BuildEvent event) {
        logger.info("Build Finished");
    }

    @Override
    public void targetStarted(BuildEvent event) {
        logger.info("Target Started: " + event.getTarget().getName());
    }

    @Override
    public void targetFinished(BuildEvent event) {
        logger.info("Target Finished: " + event.getTarget().getName());
    }

    @Override
    public void taskStarted(BuildEvent event) {
        logger.info("Task Started: " + event.getTask().getTaskName());
    }

    @Override
    public void taskFinished(BuildEvent event) {
        logger.info("Task Finished: " + event.getTask().getTaskName());
    }

    @Override
    public void messageLogged(BuildEvent event) {
        logger.info("Message: " + event.getMessage());
    }
}
