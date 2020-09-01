package com.baidu.ueditor;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONException;
import org.yaml.snakeyaml.constructor.BaseConstructor;
@RestController
@CrossOrigin
@RequestMapping("/sys/ueditor")
	public class UeditorController extends BaseConstructor{
	
 
     @RequestMapping(value = "/exec")
     @ResponseBody
     public String exec(HttpServletRequest request) throws UnsupportedEncodingException, JSONException{ 
         request.setCharacterEncoding("utf-8");
         String rootPath = request.getRealPath("/");
         return new ActionEnter( request, rootPath).exec();
     }
 }
