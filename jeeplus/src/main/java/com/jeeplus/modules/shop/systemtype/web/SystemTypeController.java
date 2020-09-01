/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemtype.web;

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
import com.jeeplus.modules.shop.systemtype.entity.SystemType;
import com.jeeplus.modules.shop.systemtype.service.SystemTypeService;

/**
 * 平台分设置Controller
 * @author lhh
 * @version 2020-01-16
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/systemtype/systemType")
public class SystemTypeController extends BaseController {

	@Autowired
	private SystemTypeService systemTypeService;
	
	@ModelAttribute
	public SystemType get(@RequestParam(required=false) String id) {
		SystemType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = systemTypeService.get(id);
		}
		if (entity == null){
			entity = new SystemType();
		}
		return entity;
	}
	
	/**
	 * 平台分类列表页面
	 */
	@RequiresPermissions("shop:systemtype:systemType:list")
	@RequestMapping(value = {"list", ""})
	public String list(SystemType systemType, Model model) {
		model.addAttribute("systemType", systemType);
		return "modules/shop/systemtype/systemTypeList";
	}
	
		/**
	 * 平台分类列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemtype:systemType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SystemType systemType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SystemType> page = systemTypeService.findPage(new Page<SystemType>(request, response), systemType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑平台分类表单页面
	 */
	@RequiresPermissions(value={"shop:systemtype:systemType:view","shop:systemtype:systemType:add","shop:systemtype:systemType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SystemType systemType, Model model) {
		model.addAttribute("systemType", systemType);
		return "modules/shop/systemtype/systemTypeForm";
	}

	/**
	 * 保存平台分类
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:systemtype:systemType:add","shop:systemtype:systemType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SystemType systemType, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(systemType);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		systemTypeService.save(systemType);//保存
		j.setSuccess(true);
		j.setMsg("保存平台分类成功");
		return j;
	}
	
	/**
	 * 删除平台分类
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemtype:systemType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SystemType systemType) {
		AjaxJson j = new AjaxJson();
		systemTypeService.delete(systemType);
		j.setMsg("删除平台分类成功");
		return j;
	}
	
	/**
	 * 批量删除平台分类
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemtype:systemType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			systemTypeService.delete(systemTypeService.get(id));
		}
		j.setMsg("删除平台分类成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemtype:systemType:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(SystemType systemType, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "平台分类"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SystemType> page = systemTypeService.findPage(new Page<SystemType>(request, response, -1), systemType);
    		new ExportExcel("平台分类", SystemType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出平台分类记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:systemtype:systemType:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SystemType> list = ei.getDataList(SystemType.class);
			for (SystemType systemType : list){
				try{
					systemTypeService.save(systemType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条平台分类记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条平台分类记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入平台分类失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入平台分类数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemtype:systemType:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "平台分类数据导入模板.xlsx";
    		List<SystemType> list = Lists.newArrayList(); 
    		new ExportExcel("平台分类数据", SystemType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}