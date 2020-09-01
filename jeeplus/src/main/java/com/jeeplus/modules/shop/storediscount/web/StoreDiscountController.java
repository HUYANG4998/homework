/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storediscount.web;

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
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.storediscount.service.StoreDiscountService;

/**
 * 商家优惠Controller
 * @author lhh
 * @version 2019-11-29
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/storediscount/storeDiscount")
public class StoreDiscountController extends BaseController {

	@Autowired
	private StoreDiscountService storeDiscountService;
	
	@ModelAttribute
	public StoreDiscount get(@RequestParam(required=false) String id) {
		StoreDiscount entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = storeDiscountService.get(id);
		}
		if (entity == null){
			entity = new StoreDiscount();
		}
		return entity;
	}
	
	/**
	 * 商家优惠列表页面
	 */
	@RequiresPermissions("shop:storediscount:storeDiscount:list")
	@RequestMapping(value = {"list", ""})
	public String list(StoreDiscount storeDiscount, Model model) {
		model.addAttribute("storeDiscount", storeDiscount);
		return "modules/shop/storediscount/storeDiscountList";
	}
	
		/**
	 * 商家优惠列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:storediscount:storeDiscount:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(StoreDiscount storeDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StoreDiscount> page = storeDiscountService.findPage(new Page<StoreDiscount>(request, response), storeDiscount); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商家优惠表单页面
	 */
	@RequiresPermissions(value={"shop:storediscount:storeDiscount:view","shop:storediscount:storeDiscount:add","shop:storediscount:storeDiscount:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(StoreDiscount storeDiscount, Model model) {
		model.addAttribute("storeDiscount", storeDiscount);
		return "modules/shop/storediscount/storeDiscountForm";
	}

	/**
	 * 保存商家优惠
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:storediscount:storeDiscount:add","shop:storediscount:storeDiscount:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(StoreDiscount storeDiscount, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(storeDiscount);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		storeDiscountService.save(storeDiscount);//保存
		j.setSuccess(true);
		j.setMsg("保存商家优惠成功");
		return j;
	}
	
	/**
	 * 删除商家优惠
	 */
	@ResponseBody
	@RequiresPermissions("shop:storediscount:storeDiscount:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(StoreDiscount storeDiscount) {
		AjaxJson j = new AjaxJson();
		storeDiscountService.delete(storeDiscount);
		j.setMsg("删除商家优惠成功");
		return j;
	}
	
	/**
	 * 批量删除商家优惠
	 */
	@ResponseBody
	@RequiresPermissions("shop:storediscount:storeDiscount:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			storeDiscountService.delete(storeDiscountService.get(id));
		}
		j.setMsg("删除商家优惠成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:storediscount:storeDiscount:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(StoreDiscount storeDiscount, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家优惠"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<StoreDiscount> page = storeDiscountService.findPage(new Page<StoreDiscount>(request, response, -1), storeDiscount);
    		new ExportExcel("商家优惠", StoreDiscount.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商家优惠记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:storediscount:storeDiscount:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StoreDiscount> list = ei.getDataList(StoreDiscount.class);
			for (StoreDiscount storeDiscount : list){
				try{
					storeDiscountService.save(storeDiscount);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商家优惠记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商家优惠记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商家优惠失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商家优惠数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:storediscount:storeDiscount:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家优惠数据导入模板.xlsx";
    		List<StoreDiscount> list = Lists.newArrayList(); 
    		new ExportExcel("商家优惠数据", StoreDiscount.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}