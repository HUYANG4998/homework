/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.store.web;

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
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;

/**
 * 商家管理Controller
 * @author lhh
 * @version 2019-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/store/store")
public class StoreController extends BaseController {

	@Autowired
	private StoreService storeService;
	
	@ModelAttribute
	public Store get(@RequestParam(required=false) String id) {
		Store entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = storeService.get(id);
		}
		if (entity == null){
			entity = new Store();
		}
		return entity;
	}
	
	/**
	 * 商家管理列表页面
	 */
	@RequiresPermissions("shop:store:store:list")
	@RequestMapping(value = {"list", ""})
	public String list(Store store, Model model) {
		model.addAttribute("store", store);
		return "modules/shop/store/storeList";
	}
	
		/**
	 * 商家管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:store:store:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Store store, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Store> page = storeService.findPage(new Page<Store>(request, response), store); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商家管理表单页面
	 */
	@RequiresPermissions(value={"shop:store:store:view","shop:store:store:add","shop:store:store:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Store store, Model model) {
		model.addAttribute("store", store);
		return "modules/shop/store/storeForm";
	}

	/**
	 * 保存商家管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:store:store:add","shop:store:store:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Store store, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(store);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		storeService.save(store);//保存
		j.setSuccess(true);
		j.setMsg("保存商家管理成功");
		return j;
	}
	
	/**
	 * 删除商家管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:store:store:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Store store) {
		AjaxJson j = new AjaxJson();
		storeService.delete(store);
		j.setMsg("删除商家管理成功");
		return j;
	}
	
	/**
	 * 批量删除商家管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:store:store:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			storeService.delete(storeService.get(id));
		}
		j.setMsg("删除商家管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:store:store:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Store store, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Store> page = storeService.findPage(new Page<Store>(request, response, -1), store);
    		new ExportExcel("商家管理", Store.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商家管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:store:store:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Store> list = ei.getDataList(Store.class);
			for (Store store : list){
				try{
					storeService.save(store);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商家管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商家管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商家管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商家管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:store:store:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家管理数据导入模板.xlsx";
    		List<Store> list = Lists.newArrayList(); 
    		new ExportExcel("商家管理数据", Store.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}