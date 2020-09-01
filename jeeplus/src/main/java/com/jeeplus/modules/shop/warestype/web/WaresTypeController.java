/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.warestype.web;

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
import com.jeeplus.modules.shop.warestype.entity.WaresType;
import com.jeeplus.modules.shop.warestype.service.WaresTypeService;

/**
 * 商品分类Controller
 * @author lhh
 * @version 2019-12-02
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/warestype/waresType")
public class WaresTypeController extends BaseController {

	@Autowired
	private WaresTypeService waresTypeService;
	
	@ModelAttribute
	public WaresType get(@RequestParam(required=false) String id) {
		WaresType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = waresTypeService.get(id);
		}
		if (entity == null){
			entity = new WaresType();
		}
		return entity;
	}
	
	/**
	 * 商品分类列表页面
	 */
	@RequiresPermissions("shop:warestype:waresType:list")
	@RequestMapping(value = {"list", ""})
	public String list(WaresType waresType, Model model) {
		model.addAttribute("waresType", waresType);
		return "modules/shop/warestype/waresTypeList";
	}
	
		/**
	 * 商品分类列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestype:waresType:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WaresType waresType, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WaresType> page = waresTypeService.findPage(new Page<WaresType>(request, response), waresType); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商品分类表单页面
	 */
	@RequiresPermissions(value={"shop:warestype:waresType:view","shop:warestype:waresType:add","shop:warestype:waresType:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WaresType waresType, Model model) {
		model.addAttribute("waresType", waresType);
		return "modules/shop/warestype/waresTypeForm";
	}

	/**
	 * 保存商品分类
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:warestype:waresType:add","shop:warestype:waresType:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(WaresType waresType, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(waresType);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		waresTypeService.save(waresType);//保存
		j.setSuccess(true);
		j.setMsg("保存商品分类成功");
		return j;
	}
	
	/**
	 * 删除商品分类
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestype:waresType:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WaresType waresType) {
		AjaxJson j = new AjaxJson();
		waresTypeService.delete(waresType);
		j.setMsg("删除商品分类成功");
		return j;
	}
	
	/**
	 * 批量删除商品分类
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestype:waresType:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			waresTypeService.delete(waresTypeService.get(id));
		}
		j.setMsg("删除商品分类成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestype:waresType:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WaresType waresType, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品分类"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WaresType> page = waresTypeService.findPage(new Page<WaresType>(request, response, -1), waresType);
    		new ExportExcel("商品分类", WaresType.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商品分类记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:warestype:waresType:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WaresType> list = ei.getDataList(WaresType.class);
			for (WaresType waresType : list){
				try{
					waresTypeService.save(waresType);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品分类记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商品分类记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商品分类失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商品分类数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:warestype:waresType:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品分类数据导入模板.xlsx";
    		List<WaresType> list = Lists.newArrayList(); 
    		new ExportExcel("商品分类数据", WaresType.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}