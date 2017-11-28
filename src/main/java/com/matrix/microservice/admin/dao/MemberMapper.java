/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.dao;

import com.matrix.microservice.admin.util.CustomerMapper;
import com.matrix.microservice.admin.entity.member.Member;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:32
 */
public interface MemberMapper extends CustomerMapper<Member> {
    Member selectByUsername(String username);
}
