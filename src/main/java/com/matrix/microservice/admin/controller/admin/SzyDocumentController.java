package com.matrix.microservice.admin.controller.admin;

import com.github.pagehelper.PageInfo;
import com.matrix.microservice.admin.conf.shiro.AdminShiroUtil;
import com.matrix.microservice.admin.entity.console.Admin;
import com.matrix.microservice.admin.entity.admin.SzyDocument;
import com.matrix.microservice.admin.service.admin.SzyDocumentService;
import com.matrix.microservice.admin.util.DateUtil;
import com.matrix.microservice.admin.util.ReturnUtil;
import com.matrix.microservice.admin.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;

import  com.matrix.microservice.admin.util.admin.DocToHtmlUtil;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/szy/document")
public class SzyDocumentController {

    @Autowired
    private SzyDocumentService szyDocumentService;
    public String DocumentPath="";

    @Autowired
    private void Path(){
        File directory = new File("");//设定为当前文件夹
        try{
            DocumentPath =directory.getAbsolutePath()+"/folder/document/";
            System.out.println(DocumentPath);
        }catch(Exception e){}
    }

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "admin/document/index";
    }

    //预览文件流
    @RequiresPermissions("document:index")
    @RequestMapping(value = "/viewer", method = {RequestMethod.GET})
    public String viewer(Model model) {
        return "admin/document/viewer";
    }
    @RequiresPermissions("document:index")
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ModelMap list(SzyDocument szyDocument) {
        ModelMap map = new ModelMap();
        List<SzyDocument> Lists=null;
        if(StringUtils.isEmpty(szyDocument.getName()) && StringUtils.isEmpty(szyDocument.getChild())
                && StringUtils.isEmpty(szyDocument.getStime())&& StringUtils.isEmpty(szyDocument.getEtime())){
            Lists = szyDocumentService.getPageList(szyDocument);
        }else {

            Lists= szyDocumentService.getBySelect(szyDocument);
        }
        map.put("pageInfo", new PageInfo<SzyDocument>(Lists));
        map.put("queryParam", szyDocument);
        return ReturnUtil.Success("加载成功", map, null);
    }


    @RequiresPermissions("document:edit")
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String add(SzyDocument szyDocument, Model model) {
        if (StringUtils.isEmpty(szyDocument.getId())) {
            szyDocument.setId("");
        }
        if (!StringUtils.isEmpty(szyDocument.getId())) {
            szyDocument = szyDocumentService.getById(szyDocument.getId());
            if (!"null".equals(szyDocument)) {
                szyDocument.setId(szyDocument.getId());
                szyDocument.setName(szyDocument.getName());
                szyDocument.setChild(szyDocument.getChild());
                szyDocument.setVer(szyDocument.getVer());
                szyDocument.setCruser(szyDocument.getCruser());
                szyDocument.setCrtime(szyDocument.getCrtime());
            }
        }

        model.addAttribute("szyDocument", szyDocument);
        return "admin/document/from";
    }


    //保存文件
    @RequiresPermissions("document:save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid SzyDocument szyDocument, @RequestParam(value = "file", required = false)MultipartFile file, BindingResult result) {
        Admin admin = AdminShiroUtil.getUserInfo();
        szyDocument.setCruser(admin.getUsername());
        szyDocument.setCrtime(DateUtil.getCurrentTime());

//        Path();//路径

        try {
            //文件名后缀
            String FileNameSuffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
            //文件名
            String FileName=file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'));
            szyDocument.setName(FileName+"."+FileNameSuffix);//set名字
            szyDocument.setSize(String.valueOf(file.getSize()));//set大小

            if (result.hasErrors()) {
                for (ObjectError er : result.getAllErrors())
                    return ReturnUtil.Error(er.getDefaultMessage(), null, null);
            }
            if (StringUtils.isEmpty(szyDocument.getId())&&file.getSize()>0) {
                String Id = UuidUtil.getUUID();
                szyDocument.setId(Id);
                new DocToHtmlUtil().Save(file.getInputStream(),DocumentPath+"/"+FileNameSuffix+"/",Id+"."+FileNameSuffix);//保存文件：输入路径 输出路径  文件名
//                new DocToHtmlUtil().WordToHtml(DocumentPath, PathHtml,FileName,FileNameSuffix);//保存html
                szyDocumentService.insert(szyDocument);//保存数据
            } else if(!StringUtils.isEmpty(szyDocument.getId())&&file.getSize()>0){
                new DocToHtmlUtil().Save(file.getInputStream(),DocumentPath+"/"+FileNameSuffix+"/",szyDocument.getId()+"."+FileNameSuffix);//保存文件
//                new DocToHtmlUtil().WordToHtml(DocumentPath, PathHtml,FileName,FileNameSuffix);//保存html
                szyDocumentService.save(szyDocument);//保存数据
            }

                return ReturnUtil.Success("操作成功", null, null);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败,重复文档", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

//读取文件流
    @RequiresPermissions("document:index")
    @ResponseBody
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public String download(@Valid SzyDocument szyDocument,Model model,HttpServletResponse response) {
        szyDocument = szyDocumentService.getById(szyDocument.getId());
        //文件名后缀
        String FileNameSuffix=szyDocument.getName().substring(szyDocument.getName().lastIndexOf('.') + 1);

//        Path();
        try {
            new DocToHtmlUtil().Read(response, DocumentPath+"/"+FileNameSuffix+"/", szyDocument.getId()+"."+FileNameSuffix,szyDocument.getName());
        }catch(Exception e){
            System.out.print("获取文件异常:"+e);
        }

        model.addAttribute("data", response);
        return "admin/document/download";
    }


    @RequiresPermissions("document:delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(String[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (String id : ids) {
                        SzyDocument szyDocument=szyDocumentService.getById(id);
                        szyDocumentService.deleteById(id);

                        String FileNameSuffix=szyDocument.getName().substring(szyDocument.getName().lastIndexOf('.') + 1);
                        new File(DocumentPath+"/"+FileNameSuffix+"/"+szyDocument.getId()+"."+FileNameSuffix).delete();
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
