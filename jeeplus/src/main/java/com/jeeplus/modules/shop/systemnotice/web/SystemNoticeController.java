/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemnotice.web;

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
import com.jeeplus.modules.shop.systemnotice.entity.SystemNotice;
import com.jeeplus.modules.shop.systemnotice.service.SystemNoticeService;

/**
 * 系统通知Controller
 * @author lhh
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/systemnotice/systemNotice")
public class SystemNoticeController extends BaseController {

	@Autowired
	private SystemNoticeService systemNoticeService;
	
	@ModelAttribute
	public SystemNotice get(@RequestParam(required=false) String id) {
		SystemNotice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = systemNoticeService.get(id);
		}
		if (entity == null){
			entity = new SystemNotice();
		}
		return entity;
	}
	
	/**
	 * 系统通知列表页面
	 */
	@RequiresPermissions("shop:systemnotice:systemNotice:list")
	@RequestMapping(value = {"list", ""})
	public String list(SystemNotice systemNotice, Model model) {
		model.addAttribute("systemNotice", systemNotice);
		return "modules/shop/systemnotice/systemNoticeList";
	}
	
		/**
	 * 系统通知列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemnotice:systemNotice:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SystemNotice systemNotice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SystemNotice> page = systemNoticeService.findPage(new Page<SystemNotice>(request, response), systemNotice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑系统通知表单页面
	 */
	@RequiresPermissions(value={"shop:systemnotice:systemNotice:view","shop:systemnotice:systemNotice:add","shop:systemnotice:systemNotice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SystemNotice systemNotice, Model model) {
		model.addAttribute("systemNotice", systemNotice);
		return "modules/shop/systemnotice/systemNoticeForm";
	}

	/**
	 * 保存系统通知
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:systemnotice:systemNotice:add","shop:systemnotice:systemNotice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SystemNotice systemNotice, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(systemNotice);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		systemNoticeService.save(systemNotice);//保存
		j.setSuccess(true);
		j.setMsg("保存系统通知成功");
		return j;
	}
	
	/**
	 * 删除系统通知
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemnotice:systemNotice:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SystemNotice systemNotice) {
		AjaxJson j = new AjaxJson();
		systemNoticeService.delete(systemNotice);
		j.setMsg("删除系统通知成功");
		return j;
	}
	
	/**
	 * 批量删除系统通知
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemnotice:systemNotice:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			systemNoticeService.delete(systemNoticeService.get(id));
		}
		j.setMsg("删除系统通知成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemnotice:systemNotice:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(SystemNotice systemNotice, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "系统通知"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SystemNotice> page = systemNoticeService.findPage(new Page<SystemNotice>(request, response, -1), systemNotice);
    		new ExportExcel("系统通知", SystemNotice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出系统通知记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:systemnotice:systemNotice:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SystemNotice> list = ei.getDataList(SystemNotice.class);
			for (SystemNotice systemNotice : list){
				try{
					systemNoticeService.save(systemNotice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条系统通知记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条系统通知记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入系统通知失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入系统通知数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemnotice:systemNotice:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "系统通知数据导入模板.xlsx";
    		List<SystemNotice> list = Lists.newArrayList(); 
    		new ExportExcel("系统通知数据", SystemNotice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}