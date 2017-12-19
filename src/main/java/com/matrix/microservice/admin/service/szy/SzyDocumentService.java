/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.service.szy;

import com.github.pagehelper.PageHelper;
import com.matrix.microservice.admin.dao.SzyDocumentMapper;
import com.matrix.microservice.admin.entity.szy.SzyDocument;
import com.matrix.microservice.admin.util.CamelCaseUtil;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author szy
 * date 2017/1/6 0006 上午 11:26
 */
@Service
public class SzyDocumentService {

    @Autowired
    private SzyDocumentMapper szyDocumentMapper;

    public List<SzyDocument> getPageList(SzyDocument szyDocument) {
        PageHelper.offsetPage(szyDocument.getOffset(),
                szyDocument.getLimit(),
                CamelCaseUtil.toUnderlineName(szyDocument.getSort())+" "+szyDocument.getOrder());
        return szyDocumentMapper.select(szyDocument);
    }
    public void deleteById(String id) {
        szyDocumentMapper.deleteByPrimaryKey(id);
    }

    public SzyDocument getById(String id) {
        return szyDocumentMapper.selectByPrimaryKey(id);
    }



    public List<SzyDocument> getBySelect(SzyDocument szyDocument) {
        PageHelper.offsetPage(szyDocument.getOffset(),
                szyDocument.getLimit(),
                CamelCaseUtil.toUnderlineName(szyDocument.getSort())+" "+szyDocument.getOrder());
        return szyDocumentMapper.selectSzyDocumentBySelect(szyDocument);
    }


    public void insert(SzyDocument szyDocument){
        szyDocumentMapper.insert(szyDocument);
    }

    public void save(SzyDocument szyDocument) {
        if (szyDocument.getId() != null) {
            szyDocumentMapper.updateByPrimaryKey(szyDocument);
        } else {
            szyDocumentMapper.insert(szyDocument);
        }
    }

}
