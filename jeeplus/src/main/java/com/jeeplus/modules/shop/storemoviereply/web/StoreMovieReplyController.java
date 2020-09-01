/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemoviereply.web;

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
import com.jeeplus.modules.shop.storemoviereply.entity.StoreMovieReply;
import com.jeeplus.modules.shop.storemoviereply.service.StoreMovieReplyService;

/**
 * 商家动态回复Controller
 * @author lhh
 * @version 2020-01-07
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/storemoviereply/storeMovieReply")
public class StoreMovieReplyController extends BaseController {

	@Autowired
	private StoreMovieReplyService storeMovieReplyService;
	
	@ModelAttribute
	public StoreMovieReply get(@RequestParam(required=false) String id) {
		StoreMovieReply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = storeMovieReplyService.get(id);
		}
		if (entity == null){
			entity = new StoreMovieReply();
		}
		return entity;
	}
	
	/**
	 * 商家动态回复列表页面
	 */
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:list")
	@RequestMapping(value = {"list", ""})
	public String list(StoreMovieReply storeMovieReply, Model model) {
		model.addAttribute("storeMovieReply", storeMovieReply);
		return "modules/shop/storemoviereply/storeMovieReplyList";
	}
	
		/**
	 * 商家动态回复列表数据
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(StoreMovieReply storeMovieReply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StoreMovieReply> page = storeMovieReplyService.findPage(new Page<StoreMovieReply>(request, response), storeMovieReply); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商家动态回复表单页面
	 */
	@RequiresPermissions(value={"shop:storemoviereply:storeMovieReply:view","shop:storemoviereply:storeMovieReply:add","shop:storemoviereply:storeMovieReply:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(StoreMovieReply storeMovieReply, Model model) {
		model.addAttribute("storeMovieReply", storeMovieReply);
		return "modules/shop/storemoviereply/storeMovieReplyForm";
	}

	/**
	 * 保存商家动态回复
	 */
	@ResponseBody
	@RequiresPermissions(value={"shop:storemoviereply:storeMovieReply:add","shop:storemoviereply:storeMovieReply:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(StoreMovieReply storeMovieReply, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(storeMovieReply);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		storeMovieReplyService.save(storeMovieReply);//保存
		j.setSuccess(true);
		j.setMsg("保存商家动态回复成功");
		return j;
	}
	
	/**
	 * 删除商家动态回复
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(StoreMovieReply storeMovieReply) {
		AjaxJson j = new AjaxJson();
		storeMovieReplyService.delete(storeMovieReply);
		j.setMsg("删除商家动态回复成功");
		return j;
	}
	
	/**
	 * 批量删除商家动态回复
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			storeMovieReplyService.delete(storeMovieReplyService.get(id));
		}
		j.setMsg("删除商家动态回复成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(StoreMovieReply storeMovieReply, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家动态回复"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<StoreMovieReply> page = storeMovieReplyService.findPage(new Page<StoreMovieReply>(request, response, -1), storeMovieReply);
    		new ExportExcel("商家动态回复", StoreMovieReply.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商家动态回复记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StoreMovieReply> list = ei.getDataList(StoreMovieReply.class);
			for (StoreMovieReply storeMovieReply : list){
				try{
					storeMovieReplyService.save(storeMovieReply);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商家动态回复记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商家动态回复记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商家动态回复失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商家动态回复数据模板
	 */
	@ResponseBody
	@RequiresPermissions("shop:storemoviereply:storeMovieReply:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商家动态回复数据导入模板.xlsx";
    		List<StoreMovieReply> list = Lists.newArrayList(); 
    		new ExportExcel("商家动态回复数据", StoreMovieReply.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}