package com.example.springscaffold2.api.dao;

import com.example.springscaffold2.api.model.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {
    public int save(String name);
    public List<Test> findAll();
}
