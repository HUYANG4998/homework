/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.wares.web;

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
import com.jeeplus.modules.shop.wares.entity.Wares;
import com.jeeplus.modules.shop.wares.service.WaresService;

/**
 * 商品管理Controller
 * @author lhh
 * @version 2019-12-18
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/wares/wares")
public class WaresController extends BaseController {

	@Autowired
	private WaresService waresService;
	
	@ModelAttribute
	public Wares get(@RequestParam(required=false) String id) {
		Wares entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = waresService.get(id);
		}
		if (entity == null){
			entity = new Wares();
		}
		return entity;
	}
	
	/**
	 * 商品管理列表页面
	 */
	@RequiresPermissions("shop:wares:wares:list")
	@RequestMapping(value = {"list", ""})
	public String list(Wares wares, Model model) {
		model.addAttribute("wares", wares);
		return "modules/shop/wares/waresList";
	}
	
		/**
	 * 商品管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:wares:wares:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Wares wares, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Wares> page = waresService.findPage(new Page<Wares>(request, response), wares); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商品管理表单页面
	 */
	@RequiresPermissions(value={"shop:wares:wares:view","shop:wares:wares:add","shop:wares:wares:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Wares wares, Model model) {
		model.addAttribute("wares", wares);
		return "modules/shop/wares/waresForm";
	}

	/**
	 * 保存商品管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:wares:wares:add","shop:wares:wares:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Wares wares, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(wares);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		waresService.save(wares);//保存
		j.setSuccess(true);
		j.setMsg("保存商品管理成功");
		return j;
	}
	
	/**
	 * 删除商品管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:wares:wares:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Wares wares) {
		AjaxJson j = new AjaxJson();
		waresService.delete(wares);
		j.setMsg("删除商品管理成功");
		return j;
	}
	
	/**
	 * 批量删除商品管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:wares:wares:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			waresService.delete(waresService.get(id));
		}
		j.setMsg("删除商品管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:wares:wares:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Wares wares, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Wares> page = waresService.findPage(new Page<Wares>(request, response, -1), wares);
    		new ExportExcel("商品管理", Wares.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商品管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:wares:wares:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Wares> list = ei.getDataList(Wares.class);
			for (Wares wares : list){
				try{
					waresService.save(wares);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商品管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商品管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商品管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商品管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:wares:wares:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商品管理数据导入模板.xlsx";
    		List<Wares> list = Lists.newArrayList(); 
    		new ExportExcel("商品管理数据", Wares.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}