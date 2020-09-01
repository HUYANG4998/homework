/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestypeattributevalue.web;

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
import com.jeeplus.modules.shop.warestypeattributevalue.entity.WaresTypeAttributeValue;
import com.jeeplus.modules.shop.warestypeattributevalue.service.WaresTypeAttributeValueService;

/**
 * 分类属性值Controller
 * @author lhh
 * @version 2019-12-02
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/warestypeattributevalue/waresTypeAttributeValue")
public class WaresTypeAttributeValueController extends BaseController {

	@Autowired
	private WaresTypeAttributeValueService waresTypeAttributeValueService;
	
	@ModelAttribute
	public WaresTypeAttributeValue get(@RequestParam(required=false) String id) {
		WaresTypeAttributeValue entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = waresTypeAttributeValueService.get(id);
		}
		if (entity == null){
			entity = new WaresTypeAttributeValue();
		}
		return entity;
	}
	
	/**
	 * 分类属性值列表页面
	 */
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:list")
	@RequestMapping(value = {"list", ""})
	public String list(WaresTypeAttributeValue waresTypeAttributeValue, Model model) {
		model.addAttribute("waresTypeAttributeValue", waresTypeAttributeValue);
		return "modules/shop/warestypeattributevalue/waresTypeAttributeValueList";
	}
	
		/**
	 * 分类属性值列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WaresTypeAttributeValue waresTypeAttributeValue, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WaresTypeAttributeValue> page = waresTypeAttributeValueService.findPage(new Page<WaresTypeAttributeValue>(request, response), waresTypeAttributeValue); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分类属性值表单页面
	 */
	@RequiresPermissions(value={"shop:warestypeattributevalue:waresTypeAttributeValue:view","shop:warestypeattributevalue:waresTypeAttributeValue:add","shop:warestypeattributevalue:waresTypeAttributeValue:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WaresTypeAttributeValue waresTypeAttributeValue, Model model) {
		model.addAttribute("waresTypeAttributeValue", waresTypeAttributeValue);
		return "modules/shop/warestypeattributevalue/waresTypeAttributeValueForm";
	}

	/**
	 * 保存分类属性值
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:warestypeattributevalue:waresTypeAttributeValue:add","shop:warestypeattributevalue:waresTypeAttributeValue:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(WaresTypeAttributeValue waresTypeAttributeValue, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(waresTypeAttributeValue);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		waresTypeAttributeValueService.save(waresTypeAttributeValue);//保存
		j.setSuccess(true);
		j.setMsg("保存分类属性值成功");
		return j;
	}
	
	/**
	 * 删除分类属性值
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WaresTypeAttributeValue waresTypeAttributeValue) {
		AjaxJson j = new AjaxJson();
		waresTypeAttributeValueService.delete(waresTypeAttributeValue);
		j.setMsg("删除分类属性值成功");
		return j;
	}
	
	/**
	 * 批量删除分类属性值
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			waresTypeAttributeValueService.delete(waresTypeAttributeValueService.get(id));
		}
		j.setMsg("删除分类属性值成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WaresTypeAttributeValue waresTypeAttributeValue, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分类属性值"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WaresTypeAttributeValue> page = waresTypeAttributeValueService.findPage(new Page<WaresTypeAttributeValue>(request, response, -1), waresTypeAttributeValue);
    		new ExportExcel("分类属性值", WaresTypeAttributeValue.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出分类属性值记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WaresTypeAttributeValue> list = ei.getDataList(WaresTypeAttributeValue.class);
			for (WaresTypeAttributeValue waresTypeAttributeValue : list){
				try{
					waresTypeAttributeValueService.save(waresTypeAttributeValue);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分类属性值记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条分类属性值记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入分类属性值失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入分类属性值数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributevalue:waresTypeAttributeValue:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分类属性值数据导入模板.xlsx";
    		List<WaresTypeAttributeValue> list = Lists.newArrayList(); 
    		new ExportExcel("分类属性值数据", WaresTypeAttributeValue.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}