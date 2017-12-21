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
public class CheckController {

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
@RequiresPermissions("businesscheck:index")
@RequestMapping(value = "/check/index", method = {RequestMethod.GET})
    public String typingindex(Model model) {
        return "admin/business/check/index";
    }

    //复核开始
    @RequiresPermissions("businesscheck:save")
    @RequestMapping(value = "/check/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid SzyBusiness szyBusiness) {
        try {

            szyBusiness=szyBusinessService.getById(szyBusiness.getId());
            szyBusiness.setChecktime(DateUtil.getCurrentTime());
            szyBusiness.setStatus("开始复核");

            szyBusinessService.save(szyBusiness);//保存数据

            SzyBusinessDetail szyBusinessDetail=new SzyBusinessDetail();
            szyBusinessDetail.setId(UuidUtil.getUUID());
            szyBusinessDetail.setLink_id(szyBusiness.getId());
            Admin admin = AdminShiroUtil.getUserInfo();
            szyBusinessDetail.setUser(admin.getUsername());
            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
            szyBusinessDetail.setStatus("开始复核");

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

    //复核结束
    @RequiresPermissions("businesscheck:save")
    @RequestMapping(value = "/check/saveend", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap saveno(@Valid SzyBusinessDetail szyBusinessDetail) {
        try {
            if ("复核成功".equals(szyBusinessDetail.getStatus()) || "复核失败".equals(szyBusinessDetail.getStatus())){
                szyBusinessDetail.setId(UuidUtil.getUUID());
                Admin admin = AdminShiroUtil.getUserInfo();
                szyBusinessDetail.setUser(admin.getUsername());
                szyBusinessDetail.setTime(DateUtil.getCurrentTime());
                szyBusinessDetailService.save(szyBusinessDetail);//保存数据

                SzyBusiness szyBusiness = szyBusinessService.getById(szyBusinessDetail.getLink_id());
                szyBusiness.setStatus(szyBusinessDetail.getStatus());
                szyBusinessService.save(szyBusiness);//保存数据

                return ReturnUtil.Success("操作成功", null, null);
            }else{
                return ReturnUtil.Error("操作失败", null, null);
            }
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequiresPermissions("businesscheck:index")
    @ResponseBody
    @RequestMapping(value = "/check/list", method = {RequestMethod.GET})
    public ModelMap list(SzyBusiness szyBusiness) {
        ModelMap map = new ModelMap();
        List<SzyBusiness> Lists=null;
        Admin admin = AdminShiroUtil.getUserInfo();
        szyBusiness.setCheckuser(admin.getUsername());
            Lists= szyBusinessService.getByCheckuser(szyBusiness);

        map.put("pageInfo", new PageInfo<SzyBusiness>(Lists));
        map.put("queryParam", szyBusiness);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequiresPermissions("businesscheck:edit")
    @RequestMapping(value = "/check/from", method = {RequestMethod.GET})
    public String add(SzyBusiness szyBusiness, Model model) {
        if (!StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness = szyBusinessService.getById(szyBusiness.getId());
            if (!"null".equals(szyBusiness)) {
                szyBusiness.setId(szyBusiness.getId());
            }
        }

        model.addAttribute("szyBusiness", szyBusiness);
        return "admin/business/check/from";
    }

    //读取文件流
    @RequiresPermissions("businesscheck:index")
    @ResponseBody
    @RequestMapping(value = "/check/download", method = {RequestMethod.GET})
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
        return "admin/business/check/download";
    }

}
