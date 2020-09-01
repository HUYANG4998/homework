/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.riderrefuse.web;

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
import com.jeeplus.modules.shop.riderrefuse.entity.RiderRefuse;
import com.jeeplus.modules.shop.riderrefuse.service.RiderRefuseService;

/**
 * 骑手拒绝订单记录Controller
 * @author lhh
 * @version 2020-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/riderrefuse/riderRefuse")
public class RiderRefuseController extends BaseController {

	@Autowired
	private RiderRefuseService riderRefuseService;
	
	@ModelAttribute
	public RiderRefuse get(@RequestParam(required=false) String id) {
		RiderRefuse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = riderRefuseService.get(id);
		}
		if (entity == null){
			entity = new RiderRefuse();
		}
		return entity;
	}
	
	/**
	 * 骑手拒绝订单记录列表页面
	 */
	@RequiresPermissions("shop:riderrefuse:riderRefuse:list")
	@RequestMapping(value = {"list", ""})
	public String list(RiderRefuse riderRefuse, Model model) {
		model.addAttribute("riderRefuse", riderRefuse);
		return "modules/shop/riderrefuse/riderRefuseList";
	}
	
		/**
	 * 骑手拒绝订单记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:riderrefuse:riderRefuse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(RiderRefuse riderRefuse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RiderRefuse> page = riderRefuseService.findPage(new Page<RiderRefuse>(request, response), riderRefuse); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑骑手拒绝订单记录表单页面
	 */
	@RequiresPermissions(value={"shop:riderrefuse:riderRefuse:view","shop:riderrefuse:riderRefuse:add","shop:riderrefuse:riderRefuse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RiderRefuse riderRefuse, Model model) {
		model.addAttribute("riderRefuse", riderRefuse);
		return "modules/shop/riderrefuse/riderRefuseForm";
	}

	/**
	 * 保存骑手拒绝订单记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:riderrefuse:riderRefuse:add","shop:riderrefuse:riderRefuse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(RiderRefuse riderRefuse, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(riderRefuse);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		riderRefuseService.save(riderRefuse);//保存
		j.setSuccess(true);
		j.setMsg("保存骑手拒绝订单记录成功");
		return j;
	}
	
	/**
	 * 删除骑手拒绝订单记录
	 */
	@ResponseBody
	@RequiresPermissions("shop:riderrefuse:riderRefuse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(RiderRefuse riderRefuse) {
		AjaxJson j = new AjaxJson();
		riderRefuseService.delete(riderRefuse);
		j.setMsg("删除骑手拒绝订单记录成功");
		return j;
	}
	
	/**
	 * 批量删除骑手拒绝订单记录
	 */
	@ResponseBody
	@RequiresPermissions("shop:riderrefuse:riderRefuse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			riderRefuseService.delete(riderRefuseService.get(id));
		}
		j.setMsg("删除骑手拒绝订单记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:riderrefuse:riderRefuse:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(RiderRefuse riderRefuse, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "骑手拒绝订单记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RiderRefuse> page = riderRefuseService.findPage(new Page<RiderRefuse>(request, response, -1), riderRefuse);
    		new ExportExcel("骑手拒绝订单记录", RiderRefuse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出骑手拒绝订单记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:riderrefuse:riderRefuse:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RiderRefuse> list = ei.getDataList(RiderRefuse.class);
			for (RiderRefuse riderRefuse : list){
				try{
					riderRefuseService.save(riderRefuse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条骑手拒绝订单记录记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条骑手拒绝订单记录记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入骑手拒绝订单记录失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入骑手拒绝订单记录数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:riderrefuse:riderRefuse:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "骑手拒绝订单记录数据导入模板.xlsx";
    		List<RiderRefuse> list = Lists.newArrayList(); 
    		new ExportExcel("骑手拒绝订单记录数据", RiderRefuse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}