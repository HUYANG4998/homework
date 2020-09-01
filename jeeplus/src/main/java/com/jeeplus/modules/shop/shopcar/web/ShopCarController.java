/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.shopcar.web;

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
import com.jeeplus.modules.shop.shopcar.entity.ShopCar;
import com.jeeplus.modules.shop.shopcar.service.ShopCarService;

/**
 * 购物车管理Controller
 * @author lhh
 * @version 2019-12-27
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/shopcar/shopCar")
public class ShopCarController extends BaseController {

	@Autowired
	private ShopCarService shopCarService;
	
	@ModelAttribute
	public ShopCar get(@RequestParam(required=false) String id) {
		ShopCar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = shopCarService.get(id);
		}
		if (entity == null){
			entity = new ShopCar();
		}
		return entity;
	}
	
	/**
	 * 购物车管理列表页面
	 */
	@RequiresPermissions("shop:shopcar:shopCar:list")
	@RequestMapping(value = {"list", ""})
	public String list(ShopCar shopCar, Model model) {
		model.addAttribute("shopCar", shopCar);
		return "modules/shop/shopcar/shopCarList";
	}
	
		/**
	 * 购物车管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:shopcar:shopCar:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ShopCar shopCar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ShopCar> page = shopCarService.findPage(new Page<ShopCar>(request, response), shopCar); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑购物车管理表单页面
	 */
	@RequiresPermissions(value={"shop:shopcar:shopCar:view","shop:shopcar:shopCar:add","shop:shopcar:shopCar:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ShopCar shopCar, Model model) {
		model.addAttribute("shopCar", shopCar);
		return "modules/shop/shopcar/shopCarForm";
	}

	/**
	 * 保存购物车管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:shopcar:shopCar:add","shop:shopcar:shopCar:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ShopCar shopCar, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(shopCar);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		shopCarService.save(shopCar);//保存
		j.setSuccess(true);
		j.setMsg("保存购物车管理成功");
		return j;
	}
	
	/**
	 * 删除购物车管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:shopcar:shopCar:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ShopCar shopCar) {
		AjaxJson j = new AjaxJson();
		shopCarService.delete(shopCar);
		j.setMsg("删除购物车管理成功");
		return j;
	}
	
	/**
	 * 批量删除购物车管理
	 */
	@ResponseBody
	@RequiresPermissions("shop:shopcar:shopCar:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			shopCarService.delete(shopCarService.get(id));
		}
		j.setMsg("删除购物车管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:shopcar:shopCar:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ShopCar shopCar, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "购物车管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ShopCar> page = shopCarService.findPage(new Page<ShopCar>(request, response, -1), shopCar);
    		new ExportExcel("购物车管理", ShopCar.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出购物车管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:shopcar:shopCar:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ShopCar> list = ei.getDataList(ShopCar.class);
			for (ShopCar shopCar : list){
				try{
					shopCarService.save(shopCar);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条购物车管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条购物车管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入购物车管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入购物车管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:shopcar:shopCar:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "购物车管理数据导入模板.xlsx";
    		List<ShopCar> list = Lists.newArrayList(); 
    		new ExportExcel("购物车管理数据", ShopCar.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}