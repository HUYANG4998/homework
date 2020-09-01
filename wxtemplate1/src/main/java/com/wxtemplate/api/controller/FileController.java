package com.wxtemplate.api.controller;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.util.PaginationUtil;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api")
public class FileController {

	@Value("${info.imgpath}")
	private String imgpath;
	@Value("${info.imgget}")
	private String imgget;
	@RequestMapping(value="/create",method=RequestMethod.POST)
    public Map<String,Object> create( MultipartFile file){
		Map<String, Object> result=new HashMap<String,Object>();
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString();
        String dFileName = fileName;
        //保存路径
        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
        String path1=imgget+dFileName+substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath+dFileName+substring );
        System.out.println(uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
            result= PaginationUtil.generateNormalJSON(path1, 1, "");
        } catch (IOException e) {
            e.printStackTrace();
            result= PaginationUtil.generateNormalJSON(null, 0, e.getMessage());
        }
        return result;
    }
    @RequestMapping(value="/download",method=RequestMethod.POST)
    public void download(HttpServletResponse response, String fileName, String path){
        if (fileName != null) {
            //设置文件路径
            File file = new File(path);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }




	@RequestMapping(value="/uploadImage",method=RequestMethod.POST)
    public Map<String,Object> uploadImage( MultipartFile file){
		Map<String, Object> result=new HashMap<String,Object>();
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString();
        String dFileName = fileName;
        //保存路径
        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
        String path1=imgget+dFileName+substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath+dFileName+substring );
        System.out.println(uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
            Map<String,Object> result1=new HashMap<>();
            result.put("code", 0);
            result.put("msg", "上传成功");
            result1.put("src", path1);
            result1.put("title", fileName);
            result.put("data", result1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
	
	   	@RequestMapping(value = "/upfileImage", method = RequestMethod.POST)
	    public String uploadGoodsPic(@RequestParam(value = "upfile") MultipartFile upfile) {
	   		Map<String, Object> result=new HashMap<String,Object>();
	        //获取上传文件名,包含后缀
	        String originalFilename = upfile.getOriginalFilename();
	        //获取后缀
	        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
	        //保存的文件名
	        UUID uuid = UUID.randomUUID();
			String fileName = uuid.toString();
	        String dFileName = fileName;
	        //保存路径
	        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
	        String path1=imgget+dFileName+substring;
	        //生成保存文件
	        //var url=path+dFileName+substring;
	        File uploadFile = new File(imgpath+dFileName+substring );
	        System.out.println(uploadFile);
	        //将上传文件保存到路径
	        try {
	        	upfile.transferTo(uploadFile);
	        	result.put("url", path1);
	        	result.put("state", "SUCCESS");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return JSON.toJSONString(result);
	    }
    @RequestMapping(value="/layimloadImage",method=RequestMethod.POST)
    public Map<String,Object> layimloadImage( MultipartFile file){
        Map<String, Object> result=new HashMap<String,Object>();
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString();
        String dFileName = fileName;
        //保存路径
        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
        String path1=imgget+dFileName+substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath+dFileName+substring );
        System.out.println(uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
            result.put("code",0);
            result.put("msg","");
            Map<String,Object> map=new HashMap<>();
            map.put("src",path1);
            result.put("data",map);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code",1);
            result.put("msg","上传失败");
            result.put("data",null);
        }
        return result;
    }




}
