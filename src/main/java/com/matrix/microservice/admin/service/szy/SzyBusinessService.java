/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.service.szy;

import com.github.pagehelper.PageHelper;
import com.matrix.microservice.admin.entity.szy.SzyBusiness;
import com.matrix.microservice.admin.dao.SzyBusinessMapper;
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
public class SzyBusinessService {

    @Autowired
    private SzyBusinessMapper szyBusinessMapper;

    public List<SzyBusiness> getPageList(SzyBusiness szyBusiness) {
        PageHelper.offsetPage(
                szyBusiness.getOffset(),
                szyBusiness.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusiness.getSort())+" "+szyBusiness.getOrder()
        );
        return szyBusinessMapper.select(szyBusiness);
    }
    public void deleteById(String id) {
        szyBusinessMapper.deleteByPrimaryKey(id);
    }
    public void deleteByName(String name) {
        szyBusinessMapper.deleteByPrimaryKey(name);
    }

    public SzyBusiness getById(String id) {
        return szyBusinessMapper.selectByPrimaryKey(id);
    }



    public List<SzyBusiness> getBySelect(SzyBusiness szyBusiness) {
        PageHelper.offsetPage(
                szyBusiness.getOffset(),
                szyBusiness.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusiness.getSort())+" "+szyBusiness.getOrder()
        );
        return szyBusinessMapper.selectSzyBusinessBySelect(szyBusiness);

    }
    public List<SzyBusiness> getBySelectView(SzyBusiness szyBusiness) {
        PageHelper.offsetPage(
                szyBusiness.getOffset(),
                szyBusiness.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusiness.getSort())+" "+szyBusiness.getOrder()
        );
        return szyBusinessMapper.selectSzyBusinessBySelectView(szyBusiness);

    }
    public List<SzyBusiness> getByZruser(SzyBusiness szyBusiness) {
        PageHelper.offsetPage(
                szyBusiness.getOffset(),
                szyBusiness.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusiness.getSort())+" "+szyBusiness.getOrder()
        );
        return szyBusinessMapper.selectSzyBusinessByZruser(szyBusiness);
    }
    public List<SzyBusiness> getByTouser(SzyBusiness szyBusiness) {
        PageHelper.offsetPage(
                szyBusiness.getOffset(),
                szyBusiness.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusiness.getSort())+" "+szyBusiness.getOrder()
        );
        return szyBusinessMapper.selectSzyBusinessByTouser(szyBusiness);
    }
    public List<SzyBusiness> getByCheckuser(SzyBusiness szyBusiness) {
        PageHelper.offsetPage(
                szyBusiness.getOffset(),
                szyBusiness.getLimit(),
                CamelCaseUtil.toUnderlineName(szyBusiness.getSort())+" "+szyBusiness.getOrder()
        );
        return szyBusinessMapper.selectSzyBusinessByCheckuser(szyBusiness);
    }


    public void insert(SzyBusiness szyBusiness){
        szyBusinessMapper.insert(szyBusiness);
    }

    public void save(SzyBusiness szyBusiness) {
        if (szyBusiness.getId() != null) {
            szyBusinessMapper.updateByPrimaryKey(szyBusiness);
        } else {
            szyBusinessMapper.insert(szyBusiness);
        }
    }

}
