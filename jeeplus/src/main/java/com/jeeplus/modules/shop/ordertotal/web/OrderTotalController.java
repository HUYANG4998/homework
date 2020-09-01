/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.ordertotal.web;

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
import com.jeeplus.modules.shop.ordertotal.entity.OrderTotal;
import com.jeeplus.modules.shop.ordertotal.service.OrderTotalService;

/**
 * 订单统计Controller
 * @author lhh
 * @version 2020-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/ordertotal/orderTotal")
public class OrderTotalController extends BaseController {

	@Autowired
	private OrderTotalService orderTotalService;
	
	@ModelAttribute
	public OrderTotal get(@RequestParam(required=false) String id) {
		OrderTotal entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderTotalService.get(id);
		}
		if (entity == null){
			entity = new OrderTotal();
		}
		return entity;
	}
	
	/**
	 * 订单统计列表页面
	 */
	@RequiresPermissions("shop:ordertotal:orderTotal:list")
	@RequestMapping(value = {"list", ""})
	public String list(OrderTotal orderTotal, Model model) {
		model.addAttribute("orderTotal", orderTotal);
		return "modules/shop/ordertotal/orderTotalList";
	}
	
		/**
	 * 订单统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:ordertotal:orderTotal:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(OrderTotal orderTotal, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderTotal> page = orderTotalService.findPage(new Page<OrderTotal>(request, response), orderTotal); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑订单统计表单页面
	 */
	@RequiresPermissions(value={"shop:ordertotal:orderTotal:view","shop:ordertotal:orderTotal:add","shop:ordertotal:orderTotal:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderTotal orderTotal, Model model) {
		model.addAttribute("orderTotal", orderTotal);
		return "modules/shop/ordertotal/orderTotalForm";
	}

	/**
	 * 保存订单统计
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:ordertotal:orderTotal:add","shop:ordertotal:orderTotal:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(OrderTotal orderTotal, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(orderTotal);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		orderTotalService.save(orderTotal);//保存
		j.setSuccess(true);
		j.setMsg("保存订单统计成功");
		return j;
	}
	
	/**
	 * 删除订单统计
	 */
	@ResponseBody
	@RequiresPermissions("shop:ordertotal:orderTotal:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(OrderTotal orderTotal) {
		AjaxJson j = new AjaxJson();
		orderTotalService.delete(orderTotal);
		j.setMsg("删除订单统计成功");
		return j;
	}
	
	/**
	 * 批量删除订单统计
	 */
	@ResponseBody
	@RequiresPermissions("shop:ordertotal:orderTotal:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			orderTotalService.delete(orderTotalService.get(id));
		}
		j.setMsg("删除订单统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:ordertotal:orderTotal:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(OrderTotal orderTotal, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OrderTotal> page = orderTotalService.findPage(new Page<OrderTotal>(request, response, -1), orderTotal);
    		new ExportExcel("订单统计", OrderTotal.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出订单统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:ordertotal:orderTotal:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderTotal> list = ei.getDataList(OrderTotal.class);
			for (OrderTotal orderTotal : list){
				try{
					orderTotalService.save(orderTotal);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单统计记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条订单统计记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入订单统计失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入订单统计数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:ordertotal:orderTotal:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单统计数据导入模板.xlsx";
    		List<OrderTotal> list = Lists.newArrayList(); 
    		new ExportExcel("订单统计数据", OrderTotal.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}