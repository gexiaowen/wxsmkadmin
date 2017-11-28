/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.service.console;


import com.matrix.microservice.admin.dao.LogMapper;
import com.matrix.microservice.admin.entity.console.Log;
import com.matrix.microservice.admin.util.CamelCaseUtil;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author geekcattle
 * date 2017/1/6 0006 上午 11:26
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    public List<Log> getPageList(Log log) {
        PageHelper.offsetPage(log.getOffset(),
                log.getLimit(),
                CamelCaseUtil.toUnderlineName(log.getSort())+" "+log.getOrder());
        return logMapper.select(log);
    }

    public void insert(Log log){
        logMapper.insert(log);
    }

    public void insertLoginLog(String username, String ip, String action){
        Log  log = new Log();
        log.setLogId(UuidUtil.getUUID());
        log.setLogUser(username);
        log.setLogTime(DateUtil.getCurrentTime());
        log.setLogIp(ip);
        log.setLogAction(action);
        this.insert(log);
    }


}
