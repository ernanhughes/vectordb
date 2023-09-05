package com.fodala.ant.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.search.*;

import java.util.HashMap;
import java.util.Map;


public class RedisSearchTask extends Task {
    private static final Logger logger = LoggerFactory.getLogger(RedisSearchTask.class);

    private String message;
    public void setMessage(String message) {
        this.message = message;
    }

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

    private static Map<String, String> toMap(String... values) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return map;
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected != actual) {
            logger.error("Expected " + expected + " but got " + actual);
        }
    }
    @Override
    public void execute() throws BuildException {
        String address = host + ':' + port;
        HostAndPort hnp = HostAndPort.from(address);
        UnifiedJedis client = new UnifiedJedis(hnp);
        String index = "testindex";


        Schema sc = new Schema().addTextField("first", 1.0).addTextField("last", 1.0).addNumericField("age");
        IndexDefinition rule = new IndexDefinition()
                .setFilter("@age>16")
                .setPrefixes(new String[]{"student:", "pupil:"});

        client.ftCreate(index, IndexOptions.defaultOptions().setDefinition(rule), sc);

        client.hset("profesor:5555", toMap("first", "Albert", "last", "Blue", "age", "55"));
        client.hset("student:1111", toMap("first", "Joe", "last", "Dod", "age", "18"));
        client.hset("pupil:2222", toMap("first", "Jen", "last", "Rod", "age", "14"));
        client.hset("student:3333", toMap("first", "El", "last", "Mark", "age", "17"));
        client.hset("pupil:4444", toMap("first", "Pat", "last", "Shu", "age", "21"));
        client.hset("student:5555", toMap("first", "Joen", "last", "Ko", "age", "20"));
        client.hset("teacher:6666", toMap("first", "Pat", "last", "Rod", "age", "20"));

        SearchResult noFilters = client.ftSearch(index, new Query());
        assertEquals(4, noFilters.getTotalResults());

        SearchResult res1 = client.ftSearch(index, new Query("@first:Jo*"));
        assertEquals(2, res1.getTotalResults());

        SearchResult res2 = client.ftSearch(index, new Query("@first:Pat"));
        assertEquals(1, res2.getTotalResults());

        SearchResult res3 = client.ftSearch(index, new Query("@last:Rod"));
        assertEquals(0, res3.getTotalResults());
    }
}
