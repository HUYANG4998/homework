/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.rider.web;

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
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;

/**
 * 骑手管理Controller
 * @author lhh
 * @version 2020-01-16
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/rider/rider")
public class RiderController extends BaseController {

	@Autowired
	private RiderService riderService;
	
	@ModelAttribute
	public Rider get(@RequestParam(required=false) String id) {
		Rider entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = riderService.get(id);
		}
		if (entity == null){
			entity = new Rider();
		}
		return entity;
	}
	
	/**
	 * 骑手管理列表页面
	 */
	@RequiresPermissions("shop:rider:rider:list")
	@RequestMapping(value = {"list", ""})
	public String list(Rider rider, Model model) {
		model.addAttribute("rider", rider);
		return "modules/shop/rider/riderList";
	}
	
		/**
	 * 骑手管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:rider:rider:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Rider rider, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Rider> page = riderService.findPage(new Page<Rider>(request, response), rider); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑骑手管理表单页面
	 */
	@RequiresPermissions(value={"shop:rider:rider:view","shop:rider:rider:add","shop:rider:rider:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Rider rider, Model model) {
		model.addAttribute("rider", rider);
		return "modules/shop/rider/riderForm";
	}

	/**
	 * 保存骑手管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:rider:rider:add","shop:rider:rider:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Rider rider, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(rider);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		riderService.save(rider);//保存
		j.setSuccess(true);
		j.setMsg("保存骑手管理成功");
		return j;
	}
	
	/**
	 * 删除骑手管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:rider:rider:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Rider rider) {
		AjaxJson j = new AjaxJson();
		riderService.delete(rider);
		j.setMsg("删除骑手管理成功");
		return j;
	}
	
	/**
	 * 批量删除骑手管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:rider:rider:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			riderService.delete(riderService.get(id));
		}
		j.setMsg("删除骑手管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:rider:rider:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Rider rider, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "骑手管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Rider> page = riderService.findPage(new Page<Rider>(request, response, -1), rider);
    		new ExportExcel("骑手管理", Rider.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出骑手管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:rider:rider:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Rider> list = ei.getDataList(Rider.class);
			for (Rider rider : list){
				try{
					riderService.save(rider);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条骑手管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条骑手管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入骑手管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入骑手管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:rider:rider:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "骑手管理数据导入模板.xlsx";
    		List<Rider> list = Lists.newArrayList(); 
    		new ExportExcel("骑手管理数据", Rider.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}