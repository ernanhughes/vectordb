package com.fodala.mapper;

import com.fodala.pojo.ErrorLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorLogMapper {
    void insert(ErrorLog errorLog);

    ErrorLog findById(int rowId);

    List<ErrorLog> all();

    void update(ErrorLog errorLog);

    void delete(int rowId);
}