/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerfollow.web;

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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.jeeplus.modules.shop.customerfollow.entity.CustomerFollow;
import com.jeeplus.modules.shop.customerfollow.service.CustomerFollowService;

/**
 * 用户关注店铺管理Controller
 * @author lhh
 * @version 2019-12-23
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/customerfollow/customerFollow")
public class CustomerFollowController extends BaseController {

	@Autowired
	private CustomerFollowService customerFollowService;
	
	@ModelAttribute
	public CustomerFollow get(@RequestParam(required=false) String id) {
		CustomerFollow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerFollowService.get(id);
		}
		if (entity == null){
			entity = new CustomerFollow();
		}
		return entity;
	}
	
	/**
	 * 用户关注店铺列表页面
	 */
	@RequiresPermissions("shop:customerfollow:customerFollow:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerFollow customerFollow, Model model) {
		model.addAttribute("customerFollow", customerFollow);
		return "modules/shop/customerfollow/customerFollowList";
	}
	
		/**
	 * 用户关注店铺列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerfollow:customerFollow:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CustomerFollow customerFollow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerFollow> page = customerFollowService.findPage(new Page<CustomerFollow>(request, response), customerFollow); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户关注店铺表单页面
	 */
	@RequiresPermissions(value={"shop:customerfollow:customerFollow:view","shop:customerfollow:customerFollow:add","shop:customerfollow:customerFollow:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, CustomerFollow customerFollow, Model model) {
		model.addAttribute("customerFollow", customerFollow);
		model.addAttribute("mode", mode);
		return "modules/shop/customerfollow/customerFollowForm";
	}

	/**
	 * 保存用户关注店铺
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:customerfollow:customerFollow:add","shop:customerfollow:customerFollow:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CustomerFollow customerFollow, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(customerFollow);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		customerFollowService.save(customerFollow);//保存
		j.setSuccess(true);
		j.setMsg("保存用户关注店铺成功");
		return j;
	}
	
	/**
	 * 删除用户关注店铺
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerfollow:customerFollow:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CustomerFollow customerFollow) {
		AjaxJson j = new AjaxJson();
		customerFollowService.delete(customerFollow);
		j.setMsg("删除用户关注店铺成功");
		return j;
	}
	
	/**
	 * 批量删除用户关注店铺
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerfollow:customerFollow:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerFollowService.delete(customerFollowService.get(id));
		}
		j.setMsg("删除用户关注店铺成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerfollow:customerFollow:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CustomerFollow customerFollow, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户关注店铺"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerFollow> page = customerFollowService.findPage(new Page<CustomerFollow>(request, response, -1), customerFollow);
    		new ExportExcel("用户关注店铺", CustomerFollow.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户关注店铺记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:customerfollow:customerFollow:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerFollow> list = ei.getDataList(CustomerFollow.class);
			for (CustomerFollow customerFollow : list){
				try{
					customerFollowService.save(customerFollow);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户关注店铺记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条用户关注店铺记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入用户关注店铺失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入用户关注店铺数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerfollow:customerFollow:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户关注店铺数据导入模板.xlsx";
    		List<CustomerFollow> list = Lists.newArrayList(); 
    		new ExportExcel("用户关注店铺数据", CustomerFollow.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}