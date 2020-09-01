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
import com.jeeplus.modules.shop.systemyin.entity.SysyemYin;
import com.jeeplus.modules.shop.systemyin.service.SysyemYinService;

/**
 * 隐私政策Controller
 * @author lhh
 * @version 2019-11-27
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/systemyin/sysyemYin")
public class SysyemYinController extends BaseController {

	@Autowired
	private SysyemYinService sysyemYinService;
	
	@ModelAttribute
	public SysyemYin get(@RequestParam(required=false) String id) {
		SysyemYin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysyemYinService.get(id);
		}
		if (entity == null){
			entity = new SysyemYin();
		}
		return entity;
	}
	
	/**
	 * 隐私政策列表页面
	 */
	@RequiresPermissions("shop:systemyin:sysyemYin:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysyemYin sysyemYin, Model model) {
		model.addAttribute("sysyemYin", sysyemYin);
		return "modules/shop/systemyin/sysyemYinList";
	}
	
		/**
	 * 隐私政策列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:sysyemYin:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SysyemYin sysyemYin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysyemYin> page = sysyemYinService.findPage(new Page<SysyemYin>(request, response), sysyemYin); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑隐私政策表单页面
	 */
	@RequiresPermissions(value={"shop:systemyin:sysyemYin:view","shop:systemyin:sysyemYin:add","shop:systemyin:sysyemYin:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysyemYin sysyemYin, Model model) {
		model.addAttribute("sysyemYin", sysyemYin);
		return "modules/shop/systemyin/sysyemYinForm";
	}

	/**
	 * 保存隐私政策
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:systemyin:sysyemYin:add","shop:systemyin:sysyemYin:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SysyemYin sysyemYin, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(sysyemYin);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		sysyemYinService.save(sysyemYin);//保存
		j.setSuccess(true);
		j.setMsg("保存隐私政策成功");
		return j;
	}
	
	/**
	 * 删除隐私政策
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:sysyemYin:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SysyemYin sysyemYin) {
		AjaxJson j = new AjaxJson();
		sysyemYinService.delete(sysyemYin);
		j.setMsg("删除隐私政策成功");
		return j;
	}
	
	/**
	 * 批量删除隐私政策
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:sysyemYin:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysyemYinService.delete(sysyemYinService.get(id));
		}
		j.setMsg("删除隐私政策成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:systemyin:sysyemYin:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(SysyemYin sysyemYin, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "隐私政策"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysyemYin> page = sysyemYinService.findPage(new Page<SysyemYin>(request, response, -1), sysyemYin);
    		new ExportExcel("隐私政策", SysyemYin.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("shop:systemyin:sysyemYin:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysyemYin> list = ei.getDataList(SysyemYin.class);
			for (SysyemYin sysyemYin : list){
				try{
					sysyemYinService.save(sysyemYin);
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
	@RequiresPermissions("shop:systemyin:sysyemYin:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "隐私政策数据导入模板.xlsx";
    		List<SysyemYin> list = Lists.newArrayList(); 
    		new ExportExcel("隐私政策数据", SysyemYin.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}