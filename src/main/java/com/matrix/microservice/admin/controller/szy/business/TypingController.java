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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/szy/business")
public class TypingController {

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

//录入
    @RequiresPermissions("businesstyping:index")
    @RequestMapping(value = "/typing/index", method = {RequestMethod.GET})
    public String typingindex(Model model) {
        return "szy/business/typing/index";
    }

    //编辑
    @RequiresPermissions("businesstyping:edit")
    @RequestMapping(value = "/typing/from", method = {RequestMethod.GET})
    public String typingadd(SzyBusiness szyBusiness, Model model) {
        if (StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness.setId("");
            model.addAttribute("szyBusiness", szyBusiness);
            return "szy/business/typing/from";
        }
        else if (!StringUtils.isEmpty(szyBusiness.getId())) {
            szyBusiness = szyBusinessService.getById(szyBusiness.getId());
            if (!"null".equals(szyBusiness)&&"未指派".equals(szyBusiness.getStatus())) {
                szyBusiness.setId(szyBusiness.getId());
                szyBusiness.setName(szyBusiness.getName());
                szyBusiness.setChild(szyBusiness.getChild());
                szyBusiness.setVer(szyBusiness.getVer());
                szyBusiness.setBranch(szyBusiness.getBranch());
                szyBusiness.setTeller(szyBusiness.getTeller());
                szyBusiness.setZruser(szyBusiness.getZruser());
                szyBusiness.setZrtime(szyBusiness.getZrtime());
                szyBusiness.setCruser(szyBusiness.getCruser());
                szyBusiness.setCrtime(szyBusiness.getCrtime());
                szyBusiness.setBak(szyBusiness.getBak());
                model.addAttribute("szyBusiness", szyBusiness);
                return "szy/business/typing/from";
            }
        }else {
            return "error";
        }
        return "error";

    }

    //录入保存
    @RequiresPermissions("businesstyping:save")
    @RequestMapping(value = "/typing/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid SzyBusiness szyBusiness, @RequestParam(value = "file", required = false)MultipartFile file, BindingResult result) {
        String flag="录入";
        if (!StringUtils.isEmpty(szyBusiness.getId())) {
            String name=szyBusiness.getName();
            String child=szyBusiness.getChild();
            String ver=szyBusiness.getVer();
            String branch=szyBusiness.getBranch();
            String teller=szyBusiness.getTeller();
            String zruser=szyBusiness.getZruser();
            String zrtime=szyBusiness.getZrtime();
            String bak=szyBusiness.getBak();

            szyBusiness = szyBusinessService.getById(szyBusiness.getId());
            szyBusiness.setName(name);
            szyBusiness.setChild(child);
            szyBusiness.setVer(ver);
            szyBusiness.setBranch(branch);
            szyBusiness.setTeller(teller);
            szyBusiness.setZruser(zruser);
            szyBusiness.setZrtime(zrtime);
            szyBusiness.setBak(bak);

            flag="修改";
            if (!"未指派".equals(szyBusiness.getStatus())) {
                flag="不可操作";
            }
        }
        if ("录入".equals(flag)||"修改".equals(flag)){
            Admin admin = AdminShiroUtil.getUserInfo();
            szyBusiness.setCruser(admin.getUsername());
            szyBusiness.setCrtime(DateUtil.getCurrentTime());

            szyBusiness.setStatus("未指派");

            SzyBusinessDetail szyBusinessDetail = new SzyBusinessDetail();
            szyBusinessDetail.setId(UuidUtil.getUUID());
            szyBusinessDetail.setUser(admin.getUsername());
            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
            szyBusinessDetail.setStatus(flag);

            try {
                //文件名后缀
                String FileNameSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
                //文件名
                String FileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'));
                szyBusiness.setName(FileName + "." + FileNameSuffix);//set名字
                if (result.hasErrors()) {
                    for (ObjectError er : result.getAllErrors())
                        return ReturnUtil.Error(er.getDefaultMessage(), null, null);
                }
                if (StringUtils.isEmpty(szyBusiness.getId()) && file.getSize() > 0) {
                    String Id = UuidUtil.getUUID();
                    szyBusiness.setId(Id);
                    new DocToHtmlUtil().Save(file.getInputStream(), BusinessPath + "/" + FileNameSuffix + "/", Id + "." + FileNameSuffix);//保存文件：输入路径 输出路径  文件名
                    szyBusinessService.insert(szyBusiness);//保存数据

                    szyBusinessDetail.setLink_id(Id);
                    szyBusinessDetailService.save(szyBusinessDetail);//detail保存数据

                } else if (!StringUtils.isEmpty(szyBusiness.getId()) && file.getSize() > 0) {
                    new DocToHtmlUtil().Save(file.getInputStream(), BusinessPath + "/" + FileNameSuffix + "/", szyBusiness.getId() + "." + FileNameSuffix);//保存文件
                    szyBusinessService.save(szyBusiness);//保存数据

                    szyBusinessDetail.setLink_id(szyBusiness.getId());
                    szyBusinessDetailService.save(szyBusinessDetail);//detail保存数据

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
        return ReturnUtil.Error("操作失败", null, null);
    }





    @RequiresPermissions("businesstyping:index")
    @ResponseBody
    @RequestMapping(value = "/typing/list", method = {RequestMethod.GET})
    public ModelMap list(SzyBusiness szyBusiness) {
        ModelMap map = new ModelMap();
        List<SzyBusiness> Lists=null;
            Lists= szyBusinessService.getBySelect(szyBusiness);
        map.put("pageInfo", new PageInfo<SzyBusiness>(Lists));
        map.put("queryParam", szyBusiness);
        return ReturnUtil.Success("加载成功", map, null);
    }




    //读取文件流
    @RequiresPermissions("businesstyping:index")
    @ResponseBody
    @RequestMapping(value = "/typing/download", method = {RequestMethod.GET})
    public String download(@Valid SzyBusiness szyBusiness,Model model,HttpServletResponse response) {
        szyBusiness = szyBusinessService.getById(szyBusiness.getId());
        //文件名后缀
        String FileNameSuffix=szyBusiness.getName().substring(szyBusiness.getName().lastIndexOf('.') + 1);

        try {
            new DocToHtmlUtil().Read(response, BusinessPath+"/"+FileNameSuffix+"/", szyBusiness.getId()+"."+FileNameSuffix,szyBusiness.getName());
        }catch(Exception e){
            System.out.print("获取文件异常:"+e);
        }

        model.addAttribute("data", response);
        return "szy/business/typing/download";
    }


    @RequiresPermissions("businesstyping:delete")
    @RequestMapping(value = "/typing/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(String[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (String id : ids) {
                        SzyBusiness szyBusiness = szyBusinessService.getById(id);
                        if ("未指派".equals(szyBusiness.getStatus())) {
                            SzyBusinessDetail szyBusinessDetail = new SzyBusinessDetail();
                            szyBusinessDetail.setId(UuidUtil.getUUID());
                            szyBusinessDetail.setLink_id(id);
                            Admin admin = AdminShiroUtil.getUserInfo();
                            szyBusinessDetail.setUser(admin.getUsername());
                            szyBusinessDetail.setTime(DateUtil.getCurrentTime());
                            szyBusinessDetail.setStatus("已删除");
                            szyBusinessDetailService.save(szyBusinessDetail);//detail保存数据

                            szyBusiness.setStatus("已删除");
                            szyBusinessService.save(szyBusiness);

                            String FileNameSuffix = szyBusiness.getName().substring(szyBusiness.getName().lastIndexOf('.') + 1);
                            new File(BusinessPath + "/" + FileNameSuffix + "/" + szyBusiness.getId() + "." + FileNameSuffix).delete();
                            return ReturnUtil.Success("删除成功", null, null);

                        }
                        return ReturnUtil.Error("不可删除", null, null);

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
