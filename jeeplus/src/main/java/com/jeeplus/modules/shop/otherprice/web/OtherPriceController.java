/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.otherprice.web;

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
import com.jeeplus.modules.shop.otherprice.entity.OtherPrice;
import com.jeeplus.modules.shop.otherprice.service.OtherPriceService;

/**
 * 配送费设置Controller
 * @author lhh
 * @version 2020-01-03
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/otherprice/otherPrice")
public class OtherPriceController extends BaseController {

	@Autowired
	private OtherPriceService otherPriceService;
	
	@ModelAttribute
	public OtherPrice get(@RequestParam(required=false) String id) {
		OtherPrice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = otherPriceService.get(id);
		}
		if (entity == null){
			entity = new OtherPrice();
		}
		return entity;
	}
	
	/**
	 * 配送费设置列表页面
	 */
	@RequiresPermissions("shop:otherprice:otherPrice:list")
	@RequestMapping(value = {"list", ""})
	public String list(OtherPrice otherPrice, Model model) {
		model.addAttribute("otherPrice", otherPrice);
		return "modules/shop/otherprice/otherPriceList";
	}
	
		/**
	 * 配送费设置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:otherprice:otherPrice:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(OtherPrice otherPrice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OtherPrice> page = otherPriceService.findPage(new Page<OtherPrice>(request, response), otherPrice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑配送费设置表单页面
	 */
	@RequiresPermissions(value={"shop:otherprice:otherPrice:view","shop:otherprice:otherPrice:add","shop:otherprice:otherPrice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OtherPrice otherPrice, Model model) {
		model.addAttribute("otherPrice", otherPrice);
		return "modules/shop/otherprice/otherPriceForm";
	}

	/**
	 * 保存配送费设置
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:otherprice:otherPrice:add","shop:otherprice:otherPrice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(OtherPrice otherPrice, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(otherPrice);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		otherPriceService.save(otherPrice);//保存
		j.setSuccess(true);
		j.setMsg("保存配送费设置成功");
		return j;
	}
	
	/**
	 * 删除配送费设置
	 */
	@ResponseBody
	@RequiresPermissions("shop:otherprice:otherPrice:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(OtherPrice otherPrice) {
		AjaxJson j = new AjaxJson();
		otherPriceService.delete(otherPrice);
		j.setMsg("删除配送费设置成功");
		return j;
	}
	
	/**
	 * 批量删除配送费设置
	 */
	@ResponseBody
	@RequiresPermissions("shop:otherprice:otherPrice:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			otherPriceService.delete(otherPriceService.get(id));
		}
		j.setMsg("删除配送费设置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:otherprice:otherPrice:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(OtherPrice otherPrice, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "配送费设置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OtherPrice> page = otherPriceService.findPage(new Page<OtherPrice>(request, response, -1), otherPrice);
    		new ExportExcel("配送费设置", OtherPrice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出配送费设置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:otherprice:otherPrice:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OtherPrice> list = ei.getDataList(OtherPrice.class);
			for (OtherPrice otherPrice : list){
				try{
					otherPriceService.save(otherPrice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条配送费设置记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条配送费设置记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入配送费设置失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入配送费设置数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:otherprice:otherPrice:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "配送费设置数据导入模板.xlsx";
    		List<OtherPrice> list = Lists.newArrayList(); 
    		new ExportExcel("配送费设置数据", OtherPrice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}