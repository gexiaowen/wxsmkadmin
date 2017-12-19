package com.matrix.microservice.admin.dao;

import com.matrix.microservice.admin.util.CustomerMapper;
import com.matrix.microservice.admin.entity.szy.SzyQuestion;

import java.util.List;

public interface SzyQuestionMapper extends CustomerMapper<SzyQuestion> {

    List<SzyQuestion> selectSzyQuestionBySelect(SzyQuestion szyQuestion);


}