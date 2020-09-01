package com.wxtemplate.wxtemplate.api.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.wxtemplate.wxtemplate.api.service.impl.FileService;
import com.wxtemplate.wxtemplate.api.util.Code;
import com.wxtemplate.wxtemplate.api.util.Test;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class FileController {

    @Value("${info.imgpath}")
    private String imgpath;
    @Value("${info.imgget}")
    private String imgget;
    //private static final String location="D:\\ffmpeg\\ffmpeg-20200601-dd76226-win64-static\\bin\\ffmpeg.exe";
    private static final String location = "/usr/local/ffmpeg/bin/ffmpeg";
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result create(MultipartFile file) {
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
        String path1 = imgget + dFileName + substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath + dFileName + substring);
        System.out.println(uploadFile);
        //将上传文件保存到路径
        fileService.fileTran(file, uploadFile);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(path1);
    }


    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public Result video(MultipartFile file) {
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
        String path1 = imgget + dFileName + substring;
        //生成保存文件
        File uploadFile = new File(imgpath + dFileName + substring);
        System.out.println(uploadFile);
        //将上传文件保存到路径
        fileService.fileTran(file, uploadFile);
        try {
            file.transferTo(uploadFile);

            //videoCatchImg(imgpath+dFileName+substring,location);
            try {
                Test.getTempPath(imgpath + dFileName + ".jpg", imgpath + dFileName + substring);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = imgget + dFileName + ".jpg";
        Map<String, Object> map = new HashMap<>();
        map.put("image", s);
        map.put("video", path1);
        return Result.success(map);
    }

    public static void videoCatchImg(String videoPath, String ffmpegPath) {
        File file = new File(videoPath);
        if (!file.exists()) {
            System.err.println("路径[" + videoPath + "]对应的视频文件不存在!");
            return;
        }
        List<String> commands = new java.util.ArrayList<String>();
        commands.add(ffmpegPath);
        //输入文件
        commands.add("-i");
        commands.add(videoPath);
        //输出文件若存在可以覆盖
        commands.add("-y");
        //指定图片编码格式
        commands.add("-f");
        commands.add("image2");
        //设置截取视频第3秒时的画面
        commands.add("-ss");
        commands.add("3");
        //截取的图片路径
        commands.add(videoPath.substring(0, videoPath.lastIndexOf(".")) + ".jpg");
        System.out.println("commands:" + commands);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public void download(HttpServletResponse response, String fileName, String path) {
        if (fileName != null) {
            //设置文件路径
            File file = new File(path);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
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


    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public Map<String, Object> uploadImage(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
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
        String path1 = imgget + dFileName + substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath + dFileName + substring);
        System.out.println(uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
            Map<String, Object> result1 = new HashMap<>();
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
        Map<String, Object> result = new HashMap<String, Object>();
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
        String path1 = imgget + dFileName + substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath + dFileName + substring);
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

    @RequestMapping(value = "/layimloadImage", method = RequestMethod.POST)
    public Map<String, Object> layimloadImage(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
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
        String path1 = imgget + dFileName + substring;
        //生成保存文件
        //var url=path+dFileName+substring;
        File uploadFile = new File(imgpath + dFileName + substring);
        System.out.println(uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
            result.put("code", 0);
            result.put("msg", "");
            Map<String, Object> map = new HashMap<>();
            map.put("src", path1);
            result.put("data", map);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 1);
            result.put("msg", "上传失败");
            result.put("data", null);
        }
        return result;
    }

    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public Result create(String fileName) {
        if (!StringUtils.isEmpty(fileName)) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            return Result.success(Code.decodeQR(imgpath+fileName));
        } else {
            return Result.fail("请添加内容");
        }

    }


}
