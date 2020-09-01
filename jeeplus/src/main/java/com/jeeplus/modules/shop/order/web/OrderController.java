/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.order.web;

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
import com.jeeplus.modules.shop.order.entity.Order;
import com.jeeplus.modules.shop.order.service.OrderService;

/**
 * 订单管理Controller
 * @author lhh
 * @version 2020-01-16
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/order/order")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;
	
	@ModelAttribute
	public Order get(@RequestParam(required=false) String id) {
		Order entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderService.get(id);
		}
		if (entity == null){
			entity = new Order();
		}
		return entity;
	}
	
	/**
	 * 订单管理列表页面
	 */
	@RequiresPermissions("shop:order:order:list")
	@RequestMapping(value = {"list", ""})
	public String list(Order order, Model model) {
		model.addAttribute("order", order);
		return "modules/shop/order/orderList";
	}
	
		/**
	 * 订单管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:order:order:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Order order, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Order> page = orderService.findPage(new Page<Order>(request, response), order); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑订单管理表单页面
	 */
	@RequiresPermissions(value={"shop:order:order:view","shop:order:order:add","shop:order:order:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Order order, Model model) {
		model.addAttribute("order", order);
		return "modules/shop/order/orderForm";
	}

	/**
	 * 保存订单管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:order:order:add","shop:order:order:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Order order, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(order);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		orderService.save(order);//保存
		j.setSuccess(true);
		j.setMsg("保存订单管理成功");
		return j;
	}
	
	/**
	 * 删除订单管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:order:order:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Order order) {
		AjaxJson j = new AjaxJson();
		orderService.delete(order);
		j.setMsg("删除订单管理成功");
		return j;
	}
	
	/**
	 * 批量删除订单管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:order:order:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			orderService.delete(orderService.get(id));
		}
		j.setMsg("删除订单管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:order:order:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Order order, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Order> page = orderService.findPage(new Page<Order>(request, response, -1), order);
    		new ExportExcel("订单管理", Order.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出订单管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public Order detail(String id) {
		return orderService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:order:order:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Order> list = ei.getDataList(Order.class);
			for (Order order : list){
				try{
					orderService.save(order);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条订单管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入订单管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入订单管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:order:order:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单管理数据导入模板.xlsx";
    		List<Order> list = Lists.newArrayList(); 
    		new ExportExcel("订单管理数据", Order.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	

}