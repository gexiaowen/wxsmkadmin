package com.matrix.microservice.admin.dao;

import com.matrix.microservice.admin.entity.admin.SzyDocument;
import com.matrix.microservice.admin.util.CustomerMapper;

import java.util.List;

public interface SzyDocumentMapper extends CustomerMapper<SzyDocument> {
    List<SzyDocument> selectSzyDocumentBySelect(SzyDocument szyDocument);


}