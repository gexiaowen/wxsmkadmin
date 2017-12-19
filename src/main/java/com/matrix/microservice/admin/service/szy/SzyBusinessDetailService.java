/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.service.szy;

import com.github.pagehelper.PageHelper;
import com.matrix.microservice.admin.entity.szy.SzyBusinessDetail;
import com.matrix.microservice.admin.dao.SzyBusinessDetailMapper;
import com.matrix.microservice.admin.util.CamelCaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author szy
 * date 2017/1/6 0006 上午 11:26
 */
@Service
public class SzyBusinessDetailService {

    @Autowired
    private SzyBusinessDetailMapper szyBusinessDetailMapper;

    public List<SzyBusinessDetail> getPageList(SzyBusinessDetail szyBusinessDetail) {
        PageHelper.offsetPage(szyBusinessDetail.getOffset(),
                szyBusinessDetail.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusinessDetail.getSort())+" "+szyBusinessDetail.getOrder());
        return szyBusinessDetailMapper.select(szyBusinessDetail);
    }
    public void deleteById(String id) {
        szyBusinessDetailMapper.deleteByPrimaryKey(id);
    }
    public void deleteByName(String name) {
        szyBusinessDetailMapper.deleteByPrimaryKey(name);
    }

    public SzyBusinessDetail getById(String id) {
        return szyBusinessDetailMapper.selectByPrimaryKey(id);
    }

    public List<SzyBusinessDetail> getBySelect(SzyBusinessDetail szyBusinessDetail) {
        PageHelper.offsetPage(szyBusinessDetail.getOffset(),
                szyBusinessDetail.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusinessDetail.getSort())+" "+szyBusinessDetail.getOrder());
        return szyBusinessDetailMapper.selectSzyBusinessDetailBySelect(szyBusinessDetail);
    }

    public void insert(SzyBusinessDetail szyBusinessDetail){
        szyBusinessDetailMapper.insert(szyBusinessDetail);
    }

    public void save(SzyBusinessDetail szyBusinessDetail) {
        szyBusinessDetailMapper.insert(szyBusinessDetail);
    }

}
