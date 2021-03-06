package com.matrix.microservice.admin.controller.admin.business;

import com.github.pagehelper.PageInfo;
import com.matrix.microservice.admin.conf.shiro.AdminShiroUtil;
import com.matrix.microservice.admin.entity.console.Admin;
import com.matrix.microservice.admin.entity.admin.SzyBusiness;
import com.matrix.microservice.admin.entity.admin.SzyBusinessDetail;
import com.matrix.microservice.admin.service.admin.SzyBusinessDetailService;
import com.matrix.microservice.admin.service.admin.SzyBusinessService;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.ReturnUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import com.matrix.microservice.admin.util.admin.DocToHtmlUtil;
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
import java.util.List;

@Controller
@RequestMapping("/szy/business")
public class AllotController {

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
@RequiresPermissions("businessallot:index")
@RequestMapping(value = "/allot/index", method = {RequestMethod.GET})
    public String typingindex(Model model) {
        return "admin/business/allot/index";
    }

    //编辑
    @RequiresPermissions("businessallot:edit")
    @RequestMapping(value = "/allot/from", method = {RequestMethod.GET})
    public String typingadd(SzyBusiness szyBusiness, Model model) {
        if (StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness.setId("");
        }
        if (!StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness = szyBusinessService.getById(szyBusiness.getId());
            if (!"null".equals(szyBusiness)) {
                szyBusiness.setId(szyBusiness.getId());
                szyBusiness.setName(szyBusiness.getName());
                szyBusiness.setChild(szyBusiness.getChild());
                szyBusiness.setVer(szyBusiness.getVer());
                szyBusiness.setTouser(szyBusiness.getTouser());
                szyBusiness.setCruser(szyBusiness.getCruser());
                szyBusiness.setCrtime(szyBusiness.getCrtime());
            }
        }

        model.addAttribute("szyBusiness", szyBusiness);
        return "admin/business/allot/from";
    }

    //指派
    @RequiresPermissions("businessallot:save")
    @RequestMapping(value = "/allot/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid SzyBusiness szyBusiness) {
        try {
            String touesr= szyBusiness.getTouser();
            String checkuesr= szyBusiness.getCheckuser();
            String bak= szyBusiness.getBak();

            szyBusiness=szyBusinessService.getById(szyBusiness.getId());
            szyBusiness.setStatus("指派完成，未处理");
            szyBusiness.setTouser(touesr);
            szyBusiness.setCheckuser(checkuesr);

            SzyBusinessDetail szyBusinessDetail=new SzyBusinessDetail();
            szyBusinessDetail.setId(UuidUtil.getUUID());
            szyBusinessDetail.setLink_id(szyBusiness.getId());
            Admin admin = AdminShiroUtil.getUserInfo();
            szyBusinessDetail.setUser(admin.getUsername());
            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
            szyBusinessDetail.setBak(bak);
            szyBusinessDetail.setStatus("指派完成，未处理");
            szyBusinessDetailService.save(szyBusinessDetail);//保存数据



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

    //关闭
    @RequiresPermissions("businessallot:close")
    @RequestMapping(value = "/allot/close", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap close(@Valid SzyBusiness szyBusiness) {
        try {
            szyBusiness=szyBusinessService.getById(szyBusiness.getId());
            szyBusiness.setStatus("已关闭");

            SzyBusinessDetail szyBusinessDetail=new SzyBusinessDetail();
            szyBusinessDetail.setId(UuidUtil.getUUID());
            szyBusinessDetail.setLink_id(szyBusiness.getId());
            Admin admin = AdminShiroUtil.getUserInfo();
            szyBusinessDetail.setUser(admin.getUsername());
            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
            szyBusinessDetail.setStatus("已关闭");
            szyBusinessDetailService.save(szyBusinessDetail);//保存数据



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
//list
    @RequiresPermissions("businessallot:index")
    @ResponseBody
    @RequestMapping(value = "/allot/list", method = {RequestMethod.GET})
    public ModelMap list(SzyBusiness szyBusiness) {
        ModelMap map = new ModelMap();
        List<SzyBusiness> Lists=null;
        Admin admin = AdminShiroUtil.getUserInfo();
        szyBusiness.setZruser(admin.getUsername());
            Lists= szyBusinessService.getByZruser(szyBusiness);

        map.put("pageInfo", new PageInfo<SzyBusiness>(Lists));
        map.put("queryParam", szyBusiness);
        return ReturnUtil.Success("加载成功", map, null);
    }


    //report编辑
    @RequiresPermissions("businessallot:index")
    @RequestMapping(value = "/allot/report", method = {RequestMethod.GET})
    public String report(SzyBusiness szyBusiness, Model model) {
        if (StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness.setId("");
        }
        if (!StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness = szyBusinessService.getById(szyBusiness.getId());
            if (!"null".equals(szyBusiness)) {
                szyBusiness.setId(szyBusiness.getId());
            }
        }

        model.addAttribute("szyBusiness", szyBusiness);
        return "admin/business/allot/report";
    }
    //reportlist
    @RequiresPermissions("businessallot:index")
    @ResponseBody
    @RequestMapping(value = "/allot/reportlist", method = {RequestMethod.GET})
    public ModelMap reportlist(SzyBusinessDetail szyBusinessDetail) {
        ModelMap map = new ModelMap();
        List<SzyBusinessDetail> Lists=null;

        Lists= szyBusinessDetailService.getBySelect(szyBusinessDetail);

        map.put("pageInfo", new PageInfo<SzyBusinessDetail>(Lists));
        map.put("queryParam", szyBusinessDetail);
        return ReturnUtil.Success("加载成功", map, null);
    }
    //读取文件流
    @RequiresPermissions("businessallot:index")
    @ResponseBody
    @RequestMapping(value = "/allot/download", method = {RequestMethod.GET})
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
        return "admin/business/allot/download";
    }

}
