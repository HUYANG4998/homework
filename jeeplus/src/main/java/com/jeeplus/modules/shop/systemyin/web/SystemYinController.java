/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.systemyin.web;

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
import com.jeeplus.modules.shop.systemyin.entity.SystemYin;
import com.jeeplus.modules.shop.systemyin.service.SystemYinService;

/**
 * 隐私政策Controller
 * @author lhh
 * @version 2019-11-27
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/systemyin/systemYin")
public class SystemYinController extends BaseController {

	@Autowired
	private SystemYinService systemYinService;
	
	@ModelAttribute
	public SystemYin get(@RequestParam(required=false) String id) {
		SystemYin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = systemYinService.get(id);
		}
		if (entity == null){
			entity = new SystemYin();
		}
		return entity;
	}
	
	/**
	 * 隐私政策列表页面
	 */
	@RequiresPermissions("shop:systemyin:systemYin:list")
	@RequestMapping(value = {"list", ""})
	public String list(SystemYin systemYin, Model model) {
		model.addAttribute("systemYin", systemYin);
		return "modules/shop/systemyin/systemYinList";
	}
	
		/**
	 * 隐私政策列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:systemYin:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SystemYin systemYin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SystemYin> page = systemYinService.findPage(new Page<SystemYin>(request, response), systemYin); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑隐私政策表单页面
	 */
	@RequiresPermissions(value={"shop:systemyin:systemYin:view","shop:systemyin:systemYin:add","shop:systemyin:systemYin:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SystemYin systemYin, Model model) {
		model.addAttribute("systemYin", systemYin);
		return "modules/shop/systemyin/systemYinForm";
	}

	/**
	 * 保存隐私政策
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:systemyin:systemYin:add","shop:systemyin:systemYin:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SystemYin systemYin, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(systemYin);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		systemYinService.save(systemYin);//保存
		j.setSuccess(true);
		j.setMsg("保存隐私政策成功");
		return j;
	}
	
	/**
	 * 删除隐私政策
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:systemYin:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SystemYin systemYin) {
		AjaxJson j = new AjaxJson();
		systemYinService.delete(systemYin);
		j.setMsg("删除隐私政策成功");
		return j;
	}
	
	/**
	 * 批量删除隐私政策
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:systemYin:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			systemYinService.delete(systemYinService.get(id));
		}
		j.setMsg("删除隐私政策成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:systemYin:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(SystemYin systemYin, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "隐私政策"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SystemYin> page = systemYinService.findPage(new Page<SystemYin>(request, response, -1), systemYin);
    		new ExportExcel("隐私政策", SystemYin.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出隐私政策记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:systemYin:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SystemYin> list = ei.getDataList(SystemYin.class);
			for (SystemYin systemYin : list){
				try{
					systemYinService.save(systemYin);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条隐私政策记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条隐私政策记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入隐私政策失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入隐私政策数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:systemYin:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "隐私政策数据导入模板.xlsx";
    		List<SystemYin> list = Lists.newArrayList(); 
    		new ExportExcel("隐私政策数据", SystemYin.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}