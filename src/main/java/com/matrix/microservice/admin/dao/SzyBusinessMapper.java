package com.matrix.microservice.admin.dao;

import com.matrix.microservice.admin.entity.szy.SzyBusiness;
import com.matrix.microservice.admin.util.CustomerMapper;

import java.util.List;

public interface SzyBusinessMapper extends CustomerMapper<SzyBusiness> {
    List<SzyBusiness> selectSzyBusinessBySelect(SzyBusiness szyBusiness);
    List<SzyBusiness> selectSzyBusinessBySelectView(SzyBusiness szyBusiness);
    List<SzyBusiness> selectSzyBusinessByZruser(SzyBusiness szyBusiness);
    List<SzyBusiness> selectSzyBusinessByTouser(SzyBusiness szyBusiness);
    List<SzyBusiness> selectSzyBusinessByCheckuser(SzyBusiness szyBusiness);
}