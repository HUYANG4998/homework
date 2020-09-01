/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerassit.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.shop.customerassit.entity.CustomerAssit;
import com.jeeplus.modules.shop.customerassit.service.CustomerAssitService;

/**
 * 用户点赞管理Controller
 * @author lhh
 * @version 2019-12-23
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/customerassit/customerAssit")
public class CustomerAssitController extends BaseController {

	@Autowired
	private CustomerAssitService customerAssitService;
	
	@ModelAttribute
	public CustomerAssit get(@RequestParam(required=false) String id) {
		CustomerAssit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerAssitService.get(id);
		}
		if (entity == null){
			entity = new CustomerAssit();
		}
		return entity;
	}
	
	/**
	 * 用户点赞列表页面
	 */
	@RequiresPermissions("shop:customerassit:customerAssit:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerAssit customerAssit, Model model) {
		model.addAttribute("customerAssit", customerAssit);
		return "modules/shop/customerassit/customerAssitList";
	}
	
		/**
	 * 用户点赞列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerassit:customerAssit:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CustomerAssit customerAssit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerAssit> page = customerAssitService.findPage(new Page<CustomerAssit>(request, response), customerAssit); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户点赞表单页面
	 */
	@RequiresPermissions(value={"shop:customerassit:customerAssit:view","shop:customerassit:customerAssit:add","shop:customerassit:customerAssit:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerAssit customerAssit, Model model) {
		model.addAttribute("customerAssit", customerAssit);
		return "modules/shop/customerassit/customerAssitForm";
	}

	/**
	 * 保存用户点赞
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:customerassit:customerAssit:add","shop:customerassit:customerAssit:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CustomerAssit customerAssit, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(customerAssit);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		customerAssitService.save(customerAssit);//保存
		j.setSuccess(true);
		j.setMsg("保存用户点赞成功");
		return j;
	}
	
	/**
	 * 删除用户点赞
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerassit:customerAssit:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CustomerAssit customerAssit) {
		AjaxJson j = new AjaxJson();
		customerAssitService.delete(customerAssit);
		j.setMsg("删除用户点赞成功");
		return j;
	}
	
	/**
	 * 批量删除用户点赞
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerassit:customerAssit:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerAssitService.delete(customerAssitService.get(id));
		}
		j.setMsg("删除用户点赞成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerassit:customerAssit:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CustomerAssit customerAssit, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户点赞"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerAssit> page = customerAssitService.findPage(new Page<CustomerAssit>(request, response, -1), customerAssit);
    		new ExportExcel("用户点赞", CustomerAssit.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户点赞记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:customerassit:customerAssit:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerAssit> list = ei.getDataList(CustomerAssit.class);
			for (CustomerAssit customerAssit : list){
				try{
					customerAssitService.save(customerAssit);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户点赞记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条用户点赞记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入用户点赞失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入用户点赞数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerassit:customerAssit:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户点赞数据导入模板.xlsx";
    		List<CustomerAssit> list = Lists.newArrayList(); 
    		new ExportExcel("用户点赞数据", CustomerAssit.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}