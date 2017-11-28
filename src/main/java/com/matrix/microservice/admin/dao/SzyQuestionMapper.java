package com.matrix.microservice.admin.dao;

import com.matrix.microservice.admin.util.CustomerMapper;
import com.matrix.microservice.admin.entity.szy.SzyQuestion;

import java.util.List;
import java.util.Map;

public interface SzyQuestionMapper extends CustomerMapper<SzyQuestion> {

    List<SzyQuestion> selectSzyQuestionBySelect(Map map);


}