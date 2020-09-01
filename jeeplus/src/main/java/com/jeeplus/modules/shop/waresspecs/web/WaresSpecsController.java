/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.waresspecs.web;

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
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;

/**
 * 商品规格Controller
 * @author lhh
 * @version 2019-12-02
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/waresspecs/waresSpecs")
public class WaresSpecsController extends BaseController {

	@Autowired
	private WaresSpecsService waresSpecsService;
	
	@ModelAttribute
	public WaresSpecs get(@RequestParam(required=false) String id) {
		WaresSpecs entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = waresSpecsService.get(id);
		}
		if (entity == null){
			entity = new WaresSpecs();
		}
		return entity;
	}
	
	/**
	 * 商品规格列表页面
	 */
	@RequiresPermissions("shop:waresspecs:waresSpecs:list")
	@RequestMapping(value = {"list", ""})
	public String list(WaresSpecs waresSpecs, Model model) {
		model.addAttribute("waresSpecs", waresSpecs);
		return "modules/shop/waresspecs/waresSpecsList";
	}
	
		/**
	 * 商品规格列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:waresspecs:waresSpecs:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WaresSpecs waresSpecs, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WaresSpecs> page = waresSpecsService.findPage(new Page<WaresSpecs>(request, response), waresSpecs); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商品规格表单页面
	 */
	@RequiresPermissions(value={"shop:waresspecs:waresSpecs:view","shop:waresspecs:waresSpecs:add","shop:waresspecs:waresSpecs:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WaresSpecs waresSpecs, Model model) {
		model.addAttribute("waresSpecs", waresSpecs);
		return "modules/shop/waresspecs/waresSpecsForm";
	}

	/**
	 * 保存商品规格
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:waresspecs:waresSpecs:add","shop:waresspecs:waresSpecs:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(WaresSpecs waresSpecs, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(waresSpecs);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		waresSpecsService.save(waresSpecs);//保存
		j.setSuccess(true);
		j.setMsg("保存商品规格成功");
		return j;
	}
	
	/**
	 * 删除商品规格
	 */
	@ResponseBody
	@RequiresPermissions("shop:waresspecs:waresSpecs:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WaresSpecs waresSpecs) {
		AjaxJson j = new AjaxJson();
		waresSpecsService.delete(waresSpecs);
		j.setMsg("删除商品规格成功");
		return j;
	}
	
	/**
	 * 批量删除商品规格
	 */
	@ResponseBody
	@RequiresPermissions("shop:waresspecs:waresSpecs:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			waresSpecsService.delete(waresSpecsService.get(id));
		}
		j.setMsg("删除商品规格成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:waresspecs:waresSpecs:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WaresSpecs waresSpecs, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品规格"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WaresSpecs> page = waresSpecsService.findPage(new Page<WaresSpecs>(request, response, -1), waresSpecs);
    		new ExportExcel("商品规格", WaresSpecs.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商品规格记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:waresspecs:waresSpecs:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WaresSpecs> list = ei.getDataList(WaresSpecs.class);
			for (WaresSpecs waresSpecs : list){
				try{
					waresSpecsService.save(waresSpecs);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品规格记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商品规格记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商品规格失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商品规格数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:waresspecs:waresSpecs:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品规格数据导入模板.xlsx";
    		List<WaresSpecs> list = Lists.newArrayList(); 
    		new ExportExcel("商品规格数据", WaresSpecs.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}