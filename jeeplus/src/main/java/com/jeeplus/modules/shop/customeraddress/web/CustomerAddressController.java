/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customeraddress.web;

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
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import com.jeeplus.modules.shop.customeraddress.service.CustomerAddressService;

/**
 * 用户收货地址管理Controller
 * @author lhh
 * @version 2019-12-23
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/customeraddress/customerAddress")
public class CustomerAddressController extends BaseController {

	@Autowired
	private CustomerAddressService customerAddressService;
	
	@ModelAttribute
	public CustomerAddress get(@RequestParam(required=false) String id) {
		CustomerAddress entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerAddressService.get(id);
		}
		if (entity == null){
			entity = new CustomerAddress();
		}
		return entity;
	}
	
	/**
	 * 用户收货地址列表页面
	 */
	@RequiresPermissions("shop:customeraddress:customerAddress:list")
	@RequestMapping(value = {"list", ""})
	public String list(CustomerAddress customerAddress, Model model) {
		model.addAttribute("customerAddress", customerAddress);
		return "modules/shop/customeraddress/customerAddressList";
	}
	
		/**
	 * 用户收货地址列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:customeraddress:customerAddress:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CustomerAddress customerAddress, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CustomerAddress> page = customerAddressService.findPage(new Page<CustomerAddress>(request, response), customerAddress); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户收货地址表单页面
	 */
	@RequiresPermissions(value={"shop:customeraddress:customerAddress:view","shop:customeraddress:customerAddress:add","shop:customeraddress:customerAddress:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CustomerAddress customerAddress, Model model) {
		model.addAttribute("customerAddress", customerAddress);
		return "modules/shop/customeraddress/customerAddressForm";
	}

	/**
	 * 保存用户收货地址
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:customeraddress:customerAddress:add","shop:customeraddress:customerAddress:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CustomerAddress customerAddress, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(customerAddress);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		customerAddressService.save(customerAddress);//保存
		j.setSuccess(true);
		j.setMsg("保存用户收货地址成功");
		return j;
	}
	
	/**
	 * 删除用户收货地址
	 */
	@ResponseBody
	@RequiresPermissions("shop:customeraddress:customerAddress:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CustomerAddress customerAddress) {
		AjaxJson j = new AjaxJson();
		customerAddressService.delete(customerAddress);
		j.setMsg("删除用户收货地址成功");
		return j;
	}
	
	/**
	 * 批量删除用户收货地址
	 */
	@ResponseBody
	@RequiresPermissions("shop:customeraddress:customerAddress:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerAddressService.delete(customerAddressService.get(id));
		}
		j.setMsg("删除用户收货地址成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:customeraddress:customerAddress:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CustomerAddress customerAddress, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户收货地址"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CustomerAddress> page = customerAddressService.findPage(new Page<CustomerAddress>(request, response, -1), customerAddress);
    		new ExportExcel("用户收货地址", CustomerAddress.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户收货地址记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:customeraddress:customerAddress:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CustomerAddress> list = ei.getDataList(CustomerAddress.class);
			for (CustomerAddress customerAddress : list){
				try{
					customerAddressService.save(customerAddress);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户收货地址记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条用户收货地址记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入用户收货地址失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入用户收货地址数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:customeraddress:customerAddress:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户收货地址数据导入模板.xlsx";
    		List<CustomerAddress> list = Lists.newArrayList(); 
    		new ExportExcel("用户收货地址数据", CustomerAddress.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}