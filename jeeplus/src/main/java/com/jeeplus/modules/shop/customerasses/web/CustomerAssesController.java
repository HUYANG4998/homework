/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customerasses.web;

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
import com.jeeplus.modules.shop.customerasses.entity.CustomerAsses;
import com.jeeplus.modules.shop.customerasses.service.CustomerAssesService;

/**
 * 商品评论Controller
 * @author lhh
 * @version 2020-01-06
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/customerasses/customerAsses")
public class CustomerAssesController extends BaseController {

	@Autowired
	private CustomerAssesService customerAssesService;
	
	@ModelAttribute
	public CustomerAsses get(@RequestParam(required=false) String id) {
		CustomerAsses entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerAssesService.get(id);
		}
		if (entity == null){
			entity = new CustomerAsses();
		}
		return entity;
	}
	
	/**
	 * 商品评论列表页面
	 */
	@RequiresPermissions("shop:customerasses:customerAsses:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerAsses customerAsses, Model model) {
		model.addAttribute("customerAsses", customerAsses);
		return "modules/shop/customerasses/customerAssesList";
	}
	
		/**
	 * 商品评论列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerasses:customerAsses:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CustomerAsses customerAsses, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerAsses> page = customerAssesService.findPage(new Page<CustomerAsses>(request, response), customerAsses); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商品评论表单页面
	 */
	@RequiresPermissions(value={"shop:customerasses:customerAsses:view","shop:customerasses:customerAsses:add","shop:customerasses:customerAsses:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerAsses customerAsses, Model model) {
		model.addAttribute("customerAsses", customerAsses);
		return "modules/shop/customerasses/customerAssesForm";
	}

	/**
	 * 保存商品评论
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:customerasses:customerAsses:add","shop:customerasses:customerAsses:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CustomerAsses customerAsses, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(customerAsses);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		customerAssesService.save(customerAsses);//保存
		j.setSuccess(true);
		j.setMsg("保存商品评论成功");
		return j;
	}
	
	/**
	 * 删除商品评论
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerasses:customerAsses:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CustomerAsses customerAsses) {
		AjaxJson j = new AjaxJson();
		customerAssesService.delete(customerAsses);
		j.setMsg("删除商品评论成功");
		return j;
	}
	
	/**
	 * 批量删除商品评论
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerasses:customerAsses:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerAssesService.delete(customerAssesService.get(id));
		}
		j.setMsg("删除商品评论成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerasses:customerAsses:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CustomerAsses customerAsses, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品评论"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerAsses> page = customerAssesService.findPage(new Page<CustomerAsses>(request, response, -1), customerAsses);
    		new ExportExcel("商品评论", CustomerAsses.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商品评论记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:customerasses:customerAsses:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerAsses> list = ei.getDataList(CustomerAsses.class);
			for (CustomerAsses customerAsses : list){
				try{
					customerAssesService.save(customerAsses);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品评论记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商品评论记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商品评论失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商品评论数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:customerasses:customerAsses:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品评论数据导入模板.xlsx";
    		List<CustomerAsses> list = Lists.newArrayList(); 
    		new ExportExcel("商品评论数据", CustomerAsses.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}