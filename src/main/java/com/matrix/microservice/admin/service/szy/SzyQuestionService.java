/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.service.szy;


import com.matrix.microservice.admin.dao.SzyQuestionMapper;
import com.matrix.microservice.admin.entity.szy.SzyQuestion;
import com.matrix.microservice.admin.util.CamelCaseUtil;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author szy
 * date 2017/1/6 0006 上午 11:26
 */
@Service
public class SzyQuestionService {

    @Autowired
    private SzyQuestionMapper szyQuestionMapper;

    public List<SzyQuestion> getPageList(SzyQuestion szyQuestion) {
        PageHelper.offsetPage(szyQuestion.getOffset(),
                szyQuestion.getLimit(),
                CamelCaseUtil.toUnderlineName(szyQuestion.getSort())+" "+szyQuestion.getOrder());
        return szyQuestionMapper.select(szyQuestion);
    }
    public void deleteById(String id) {
        szyQuestionMapper.deleteByPrimaryKey(id);
    }

    public SzyQuestion getById(String id) {
        return szyQuestionMapper.selectByPrimaryKey(id);
    }



    public List<SzyQuestion> getBySelect(SzyQuestion szyQuestion) {
        PageHelper.offsetPage(szyQuestion.getOffset(),
                szyQuestion.getLimit(),
                CamelCaseUtil.toUnderlineName(szyQuestion.getSort())+" "+szyQuestion.getOrder());
        return szyQuestionMapper.selectSzyQuestionBySelect(szyQuestion);
    }


    public void insert(SzyQuestion szyQuestion){
        szyQuestionMapper.insert(szyQuestion);
    }

    public void save(SzyQuestion szyQuestion) {
        if (szyQuestion.getId() != null) {
            szyQuestionMapper.updateByPrimaryKey(szyQuestion);
        } else {
            szyQuestionMapper.insert(szyQuestion);
        }
    }

    public void insertSzyQuestion(String username, String question, String service, String child,String deal){
        SzyQuestion  szyQuestion = new SzyQuestion();
        szyQuestion.setId(UuidUtil.getUUID());
        szyQuestion.setQuestion(question);
        szyQuestion.setService(service);
        szyQuestion.setChild(child);
        szyQuestion.setDeal(deal);
        szyQuestion.setCruser(username);
        szyQuestion.setCrtime(DateUtil.getCurrentTime());

        this.insert(szyQuestion);
    }


}
