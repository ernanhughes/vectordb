package com.fodala.service;

import java.util.Map;

public interface AntRunner {
    void run(String buildFilePath, Map<String, String> properties);
}
