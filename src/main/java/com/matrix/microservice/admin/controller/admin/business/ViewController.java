package com.matrix.microservice.admin.controller.admin.business;

import com.github.pagehelper.PageInfo;
import com.matrix.microservice.admin.entity.admin.SzyBusiness;
import com.matrix.microservice.admin.entity.admin.SzyBusinessDetail;
import com.matrix.microservice.admin.service.admin.SzyBusinessDetailService;
import com.matrix.microservice.admin.service.admin.SzyBusinessService;
import com.matrix.microservice.admin.util.ReturnUtil;
import com.matrix.microservice.admin.util.admin.DocToHtmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class ViewController {

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
//view
@RequiresPermissions("businessview:index")
@RequestMapping(value = "/view/index", method = {RequestMethod.GET})
    public String typingindex(Model model) {
        return "admin/business/view/index";
    }

//list
    @RequiresPermissions("businessview:index")
    @ResponseBody
    @RequestMapping(value = "/view/list", method = {RequestMethod.GET})
    public ModelMap list(SzyBusiness szyBusiness) {
        ModelMap map = new ModelMap();
        List<SzyBusiness> Lists=null;
        if(StringUtils.isEmpty(szyBusiness.getName()) && StringUtils.isEmpty(szyBusiness.getChild()) && StringUtils.isEmpty(szyBusiness.getVer())
                && StringUtils.isEmpty(szyBusiness.getStime())&& StringUtils.isEmpty(szyBusiness.getEtime())
                && StringUtils.isEmpty(szyBusiness.getBranch())
                && StringUtils.isEmpty(szyBusiness.getZruser())){
            Lists = szyBusinessService.getPageList(szyBusiness);
        }else  {
            Lists= szyBusinessService.getBySelectView(szyBusiness);
        }
        map.put("pageInfo", new PageInfo<SzyBusiness>(Lists));
        map.put("queryParam", szyBusiness);
        return ReturnUtil.Success("加载成功", map, null);
    }


    //report
    @RequiresPermissions("businessview:index")
    @RequestMapping(value = "/view/report", method = {RequestMethod.GET})
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
        return "admin/business/view/report";
    }
    //reportlist
    @RequiresPermissions("businessview:index")
    @ResponseBody
    @RequestMapping(value = "/view/reportlist", method = {RequestMethod.GET})
    public ModelMap reportlist(SzyBusinessDetail szyBusinessDetail) {
        ModelMap map = new ModelMap();
        List<SzyBusinessDetail> Lists=null;
        Lists= szyBusinessDetailService.getBySelect(szyBusinessDetail);
        map.put("pageInfo", new PageInfo<SzyBusinessDetail>(Lists));
        map.put("queryParam", szyBusinessDetail);
        return ReturnUtil.Success("加载成功", map, null);
    }
    //读取文件流
    @RequiresPermissions("businessview:index")
    @ResponseBody
    @RequestMapping(value = "/view/download", method = {RequestMethod.GET})
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
        return "admin/business/view/download";
    }

}
