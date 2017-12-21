package com.matrix.microservice.admin.dao;

import com.matrix.microservice.admin.entity.admin.SzyBusinessDetail;
import com.matrix.microservice.admin.util.CustomerMapper;

import java.util.List;

public interface SzyBusinessDetailMapper extends CustomerMapper<SzyBusinessDetail> {
    List<SzyBusinessDetail> selectSzyBusinessDetailBySelect(SzyBusinessDetail szyBusinessDetail);
}