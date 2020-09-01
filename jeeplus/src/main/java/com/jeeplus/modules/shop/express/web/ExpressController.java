/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.express.web;

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
import com.jeeplus.modules.shop.express.entity.Express;
import com.jeeplus.modules.shop.express.service.ExpressService;

/**
 * 快递方式配置Controller
 * @author lhh
 * @version 2020-01-15
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/express/express")
public class ExpressController extends BaseController {

	@Autowired
	private ExpressService expressService;
	
	@ModelAttribute
	public Express get(@RequestParam(required=false) String id) {
		Express entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = expressService.get(id);
		}
		if (entity == null){
			entity = new Express();
		}
		return entity;
	}
	
	/**
	 * 快递方式列表页面
	 */
	@RequiresPermissions("shop:express:express:list")
	@RequestMapping(value = {"list", ""})
	public String list(Express express, Model model) {
		model.addAttribute("express", express);
		return "modules/shop/express/expressList";
	}
	
		/**
	 * 快递方式列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:express:express:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Express express, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Express> page = expressService.findPage(new Page<Express>(request, response), express); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑快递方式表单页面
	 */
	@RequiresPermissions(value={"shop:express:express:view","shop:express:express:add","shop:express:express:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Express express, Model model) {
		model.addAttribute("express", express);
		return "modules/shop/express/expressForm";
	}

	/**
	 * 保存快递方式
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:express:express:add","shop:express:express:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Express express, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(express);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		expressService.save(express);//保存
		j.setSuccess(true);
		j.setMsg("保存快递方式成功");
		return j;
	}
	
	/**
	 * 删除快递方式
	 */
	@ResponseBody
	@RequiresPermissions("shop:express:express:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Express express) {
		AjaxJson j = new AjaxJson();
		expressService.delete(express);
		j.setMsg("删除快递方式成功");
		return j;
	}
	
	/**
	 * 批量删除快递方式
	 */
	@ResponseBody
	@RequiresPermissions("shop:express:express:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			expressService.delete(expressService.get(id));
		}
		j.setMsg("删除快递方式成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:express:express:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Express express, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "快递方式"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Express> page = expressService.findPage(new Page<Express>(request, response, -1), express);
    		new ExportExcel("快递方式", Express.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出快递方式记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:express:express:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Express> list = ei.getDataList(Express.class);
			for (Express express : list){
				try{
					expressService.save(express);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条快递方式记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条快递方式记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入快递方式失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入快递方式数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:express:express:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "快递方式数据导入模板.xlsx";
    		List<Express> list = Lists.newArrayList(); 
    		new ExportExcel("快递方式数据", Express.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}