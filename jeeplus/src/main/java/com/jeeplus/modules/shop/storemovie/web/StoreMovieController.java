/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemovie.web;

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
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.storemovie.service.StoreMovieService;

/**
 * 商家动态Controller
 * @author lhh
 * @version 2019-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/storemovie/storeMovie")
public class StoreMovieController extends BaseController {

	@Autowired
	private StoreMovieService storeMovieService;
	
	@ModelAttribute
	public StoreMovie get(@RequestParam(required=false) String id) {
		StoreMovie entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = storeMovieService.get(id);
		}
		if (entity == null){
			entity = new StoreMovie();
		}
		return entity;
	}
	
	/**
	 * 商家动态列表页面
	 */
	@RequiresPermissions("shop:storemovie:storeMovie:list")
	@RequestMapping(value = {"list", ""})
	public String list(StoreMovie storeMovie, Model model) {
		model.addAttribute("storeMovie", storeMovie);
		return "modules/shop/storemovie/storeMovieList";
	}
	
		/**
	 * 商家动态列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemovie:storeMovie:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(StoreMovie storeMovie, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StoreMovie> page = storeMovieService.findPage(new Page<StoreMovie>(request, response), storeMovie); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商家动态表单页面
	 */
	@RequiresPermissions(value={"shop:storemovie:storeMovie:view","shop:storemovie:storeMovie:add","shop:storemovie:storeMovie:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(StoreMovie storeMovie, Model model) {
		model.addAttribute("storeMovie", storeMovie);
		return "modules/shop/storemovie/storeMovieForm";
	}

	/**
	 * 保存商家动态
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:storemovie:storeMovie:add","shop:storemovie:storeMovie:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(StoreMovie storeMovie, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(storeMovie);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		storeMovieService.save(storeMovie);//保存
		j.setSuccess(true);
		j.setMsg("保存商家动态成功");
		return j;
	}
	
	/**
	 * 删除商家动态
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemovie:storeMovie:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(StoreMovie storeMovie) {
		AjaxJson j = new AjaxJson();
		storeMovieService.delete(storeMovie);
		j.setMsg("删除商家动态成功");
		return j;
	}
	
	/**
	 * 批量删除商家动态
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemovie:storeMovie:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			storeMovieService.delete(storeMovieService.get(id));
		}
		j.setMsg("删除商家动态成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemovie:storeMovie:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(StoreMovie storeMovie, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家动态"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<StoreMovie> page = storeMovieService.findPage(new Page<StoreMovie>(request, response, -1), storeMovie);
    		new ExportExcel("商家动态", StoreMovie.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商家动态记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:storemovie:storeMovie:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StoreMovie> list = ei.getDataList(StoreMovie.class);
			for (StoreMovie storeMovie : list){
				try{
					storeMovieService.save(storeMovie);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商家动态记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商家动态记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商家动态失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商家动态数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemovie:storeMovie:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家动态数据导入模板.xlsx";
    		List<StoreMovie> list = Lists.newArrayList(); 
    		new ExportExcel("商家动态数据", StoreMovie.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}