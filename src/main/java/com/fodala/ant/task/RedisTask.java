package com.fodala.ant.task;

import com.fodala.Application;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RedisTask extends Task {
    private static final Logger logger = LoggerFactory.getLogger(RedisTask.class);

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static final String REDIS_PASSWORD = "";

    protected String host = REDIS_HOST;
    protected int port = REDIS_PORT;
    protected String password = REDIS_PASSWORD;

}
