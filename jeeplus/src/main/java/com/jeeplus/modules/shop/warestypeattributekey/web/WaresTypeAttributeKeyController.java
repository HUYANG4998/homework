/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestypeattributekey.web;

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
import com.jeeplus.modules.shop.warestypeattributekey.entity.WaresTypeAttributeKey;
import com.jeeplus.modules.shop.warestypeattributekey.service.WaresTypeAttributeKeyService;

/**
 * 分类属性Controller
 * @author lhh
 * @version 2019-12-24
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/warestypeattributekey/waresTypeAttributeKey")
public class WaresTypeAttributeKeyController extends BaseController {

	@Autowired
	private WaresTypeAttributeKeyService waresTypeAttributeKeyService;
	
	@ModelAttribute
	public WaresTypeAttributeKey get(@RequestParam(required=false) String id) {
		WaresTypeAttributeKey entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = waresTypeAttributeKeyService.get(id);
		}
		if (entity == null){
			entity = new WaresTypeAttributeKey();
		}
		return entity;
	}
	
	/**
	 * 分类属性列表页面
	 */
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:list")
	@RequestMapping(value = {"list", ""})
	public String list(WaresTypeAttributeKey waresTypeAttributeKey, Model model) {
		model.addAttribute("waresTypeAttributeKey", waresTypeAttributeKey);
		return "modules/shop/warestypeattributekey/waresTypeAttributeKeyList";
	}
	
		/**
	 * 分类属性列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WaresTypeAttributeKey waresTypeAttributeKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WaresTypeAttributeKey> page = waresTypeAttributeKeyService.findPage(new Page<WaresTypeAttributeKey>(request, response), waresTypeAttributeKey); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分类属性表单页面
	 */
	@RequiresPermissions(value={"shop:warestypeattributekey:waresTypeAttributeKey:view","shop:warestypeattributekey:waresTypeAttributeKey:add","shop:warestypeattributekey:waresTypeAttributeKey:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WaresTypeAttributeKey waresTypeAttributeKey, Model model) {
		model.addAttribute("waresTypeAttributeKey", waresTypeAttributeKey);
		return "modules/shop/warestypeattributekey/waresTypeAttributeKeyForm";
	}

	/**
	 * 保存分类属性
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:warestypeattributekey:waresTypeAttributeKey:add","shop:warestypeattributekey:waresTypeAttributeKey:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(WaresTypeAttributeKey waresTypeAttributeKey, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(waresTypeAttributeKey);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		waresTypeAttributeKeyService.save(waresTypeAttributeKey);//保存
		j.setSuccess(true);
		j.setMsg("保存分类属性成功");
		return j;
	}
	
	/**
	 * 删除分类属性
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WaresTypeAttributeKey waresTypeAttributeKey) {
		AjaxJson j = new AjaxJson();
		waresTypeAttributeKeyService.delete(waresTypeAttributeKey);
		j.setMsg("删除分类属性成功");
		return j;
	}
	
	/**
	 * 批量删除分类属性
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			waresTypeAttributeKeyService.delete(waresTypeAttributeKeyService.get(id));
		}
		j.setMsg("删除分类属性成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WaresTypeAttributeKey waresTypeAttributeKey, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分类属性"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WaresTypeAttributeKey> page = waresTypeAttributeKeyService.findPage(new Page<WaresTypeAttributeKey>(request, response, -1), waresTypeAttributeKey);
    		new ExportExcel("分类属性", WaresTypeAttributeKey.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出分类属性记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WaresTypeAttributeKey> list = ei.getDataList(WaresTypeAttributeKey.class);
			for (WaresTypeAttributeKey waresTypeAttributeKey : list){
				try{
					waresTypeAttributeKeyService.save(waresTypeAttributeKey);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分类属性记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条分类属性记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入分类属性失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入分类属性数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestypeattributekey:waresTypeAttributeKey:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分类属性数据导入模板.xlsx";
    		List<WaresTypeAttributeKey> list = Lists.newArrayList(); 
    		new ExportExcel("分类属性数据", WaresTypeAttributeKey.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}