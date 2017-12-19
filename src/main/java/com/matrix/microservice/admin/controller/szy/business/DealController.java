package com.matrix.microservice.admin.controller.szy.business;

import com.github.pagehelper.PageInfo;
import com.matrix.microservice.admin.conf.shiro.AdminShiroUtil;
import com.matrix.microservice.admin.entity.console.Admin;
import com.matrix.microservice.admin.entity.szy.SzyBusiness;
import com.matrix.microservice.admin.entity.szy.SzyBusinessDetail;
import com.matrix.microservice.admin.service.szy.SzyBusinessDetailService;
import com.matrix.microservice.admin.service.szy.SzyBusinessService;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.ReturnUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import com.matrix.microservice.admin.util.szy.DocToHtmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/szy/business")
public class DealController {

    @Autowired
    private SzyBusinessService szyBusinessService;
    @Autowired
    private SzyBusinessDetailService szyBusinessDetailService;
    public String BusinessPath="";
    @Autowired
    private void Path(){
        File directory = new File("");//设定为当前文件夹
        try{
            BusinessPath =directory.getAbsolutePath()+"/folder/business/";
            System.out.println(BusinessPath);
        }catch(Exception e){}
    }
//指派
@RequiresPermissions("businessdeal:index")
@RequestMapping(value = "/deal/index", method = {RequestMethod.GET})
    public String typingindex(Model model) {
        return "szy/business/deal/index";
    }


    //开始处理
    @RequiresPermissions("businessdeal:save")
    @RequestMapping(value = "/deal/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid SzyBusiness szyBusiness) {
        try {

            szyBusiness=szyBusinessService.getById(szyBusiness.getId());
            szyBusiness.setTotime(DateUtil.getCurrentTime());
            szyBusiness.setStatus("开始处理");

             szyBusinessService.save(szyBusiness);//保存数据

            SzyBusinessDetail szyBusinessDetail=new SzyBusinessDetail();
            szyBusinessDetail.setId(UuidUtil.getUUID());
            szyBusinessDetail.setLink_id(szyBusiness.getId());
            Admin admin = AdminShiroUtil.getUserInfo();
            szyBusinessDetail.setUser(admin.getUsername());
            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
            szyBusinessDetail.setStatus("开始处理");
            szyBusinessDetailService.save(szyBusinessDetail);//保存数据



             return ReturnUtil.Success("操作成功", null, null);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }


    @RequiresPermissions("businessdeal:index")
    @ResponseBody
    @RequestMapping(value = "/deal/list", method = {RequestMethod.GET})
    public ModelMap list(SzyBusiness szyBusiness) {
        ModelMap map = new ModelMap();
        List<SzyBusiness> Lists=null;
        Admin admin = AdminShiroUtil.getUserInfo();
        szyBusiness.setTouser(admin.getUsername());

            Lists= szyBusinessService.getByTouser(szyBusiness);

        map.put("pageInfo", new PageInfo<SzyBusiness>(Lists));
        map.put("queryParam", szyBusiness);
        return ReturnUtil.Success("加载成功", map, null);
    }


    @RequiresPermissions("businessdeal:edit")
    @RequestMapping(value = "/deal/from", method = {RequestMethod.GET})
    public String add(SzyBusiness szyBusiness, Model model) {
        if (!StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness = szyBusinessService.getById(szyBusiness.getId());
            if (!"null".equals(szyBusiness)) {
                szyBusiness.setId(szyBusiness.getId());
            }
        }

        model.addAttribute("szyBusiness", szyBusiness);
        return "szy/business/deal/from";
    }

    //处理结束
    @RequiresPermissions("businessdeal:save")
    @RequestMapping(value = "/deal/saveend", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap saveend(@Valid SzyBusinessDetail szyBusinessDetail) {
        try {

            szyBusinessDetail.setId(UuidUtil.getUUID());
            Admin admin = AdminShiroUtil.getUserInfo();
            szyBusinessDetail.setUser(admin.getUsername());
            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
            szyBusinessDetail.setStatus("处理完成");
            szyBusinessDetailService.save(szyBusinessDetail);//保存数据

            SzyBusiness szyBusiness=szyBusinessService.getById(szyBusinessDetail.getLink_id());
            szyBusiness.setStatus("处理完成");
            szyBusinessService.save(szyBusiness);//保存数据

            return ReturnUtil.Success("操作成功", null, null);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
    
    
    
    
    //读取文件流
    @RequiresPermissions("businessdeal:index")
    @ResponseBody
    @RequestMapping(value = "/deal/download", method = {RequestMethod.GET})
    public String download(@Valid SzyBusiness szyBusiness,Model model,HttpServletResponse response) {
        szyBusiness = szyBusinessService.getById(szyBusiness.getId());
        //文件名后缀
        String FileNameSuffix=szyBusiness.getName().substring(szyBusiness.getName().lastIndexOf('.') + 1);

//        Path();
        try {
            new DocToHtmlUtil().Read(response, BusinessPath+"/"+FileNameSuffix+"/", szyBusiness.getId()+"."+FileNameSuffix,szyBusiness.getName());
        }catch(Exception e){
            System.out.print("获取文件异常:"+e);
        }

        model.addAttribute("data", response);
        return "szy/business/deal/download";
    }

}
