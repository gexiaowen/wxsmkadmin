/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.matrix.microservice.admin.controller.szy;

import com.matrix.microservice.admin.conf.shiro.AdminShiroUtil;
import com.matrix.microservice.admin.entity.console.Admin;
import com.matrix.microservice.admin.entity.szy.SzyQuestion;
import com.matrix.microservice.admin.service.szy.SzyQuestionService;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.ReturnUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author geekcattle
 * date 2017/1/6 0006 上午 11:35
 */
@Controller
@RequestMapping("/szy/question")
public class SzyQuestionController {

    @Autowired
    private SzyQuestionService szyQuestionService;


    private String sql;
    private String service;
    private String child;
    private String question;


    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "szy/question/index";
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ModelMap list(SzyQuestion szyQuestion) {
        ModelMap map = new ModelMap();
        Map<String, Object> parammap = new HashMap<String, Object>();
        List<SzyQuestion> Lists=null;
        if(StringUtils.isEmpty(szyQuestion.getService()) && StringUtils.isEmpty(szyQuestion.getChild()) && StringUtils.isEmpty(szyQuestion.getQuestion())){
            Lists = szyQuestionService.getPageList(szyQuestion);
        }else {
            parammap.put("service",StringUtils.isEmpty(szyQuestion.getService())?"":szyQuestion.getService());
            parammap.put("child",StringUtils.isEmpty(szyQuestion.getChild())?"":szyQuestion.getChild());
            parammap.put("question",StringUtils.isEmpty(szyQuestion.getQuestion())?"":szyQuestion.getQuestion());
            parammap.put("order", szyQuestion.getOrder());
            parammap.put("offset",szyQuestion.getOffset());
            parammap.put("limit",szyQuestion.getLimit());

            Lists= szyQuestionService.getBySelect(parammap);


        }

        map.put("pageInfo", new PageInfo<SzyQuestion>(Lists));
        map.put("queryParam", szyQuestion);
        return ReturnUtil.Success("加载成功", map, null);
    }


    @RequiresPermissions("admin:edit")
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String add(SzyQuestion szyQuestion, Model model) {
        if (StringUtils.isEmpty(szyQuestion.getId())) {
            szyQuestion.setId("");
        }
        if (!StringUtils.isEmpty(szyQuestion.getId())) {
            szyQuestion = szyQuestionService.getById(szyQuestion.getId());
            if (!"null".equals(szyQuestion)) {
                szyQuestion.setId(szyQuestion.getId());
                szyQuestion.setQuestion(szyQuestion.getQuestion());
                szyQuestion.setService(szyQuestion.getService());
                szyQuestion.setChild(szyQuestion.getChild());
                szyQuestion.setDeal(szyQuestion.getDeal().replaceAll("<br>","\r\n"));
                szyQuestion.setCruser(szyQuestion.getCruser());
                szyQuestion.setCrtime(szyQuestion.getCrtime());
            }
            }

        model.addAttribute("szyQuestion", szyQuestion);
        return "szy/question/from";
    }

    @RequiresPermissions("menu:save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid SzyQuestion szyQuestion, BindingResult result) {
        Admin admin = AdminShiroUtil.getUserInfo();
        szyQuestion.setCruser(admin.getUsername());
        szyQuestion.setCrtime(DateUtil.getCurrentTime());
        szyQuestion.setDeal(szyQuestion.getDeal().replaceAll("\r\n","<br>"));

        try {
            if (result.hasErrors()) {
                for (ObjectError er : result.getAllErrors())
                    return ReturnUtil.Error(er.getDefaultMessage(), null, null);
            }
            if (StringUtils.isEmpty(szyQuestion.getId())) {
                String Id = UuidUtil.getUUID();
                szyQuestion.setId(Id);
                szyQuestionService.insert(szyQuestion);
            } else {
                szyQuestionService.save(szyQuestion);
            }

            return ReturnUtil.Success("操作成功", null, "/szy/question/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequiresPermissions("admin:delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(String[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (String id : ids) {
                        szyQuestionService.deleteById(id);
                    }
                }
                return ReturnUtil.Success("删除成功", null, null);
            } else {
                return ReturnUtil.Error("删除失败", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("删除失败", null, null);
        }
    }

}
