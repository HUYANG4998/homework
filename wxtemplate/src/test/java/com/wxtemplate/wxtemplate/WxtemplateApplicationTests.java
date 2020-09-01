package com.wxtemplate.wxtemplate;

import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WxtemplateApplicationTests {

    @Autowired
    private UserServiceImpl userService;
 /*   public static void main(String[] args){
        double randomMoney = getRandomMoney(10, 100.00);
        System.out.println(randomMoney);
        System.out.println("123");
    }*/
    @Test
    void contextLoads() {
        List<User> userList=new ArrayList<>();
        userService.digui("713453255488831488",4,userList);
        for (User user:userList){
            System.out.println(user.getUserId());
        }
    }
    public static void videoCatchImg(String videoPath, String ffmpegPath) {
        File file = new File(videoPath);
        if (!file.exists()) {
            System.err.println("路径[" + videoPath + "]对应的视频文件不存在!");
            return ;
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
        commands.add(videoPath.substring(0, videoPath.lastIndexOf(".")) + "_cover.jpg");
        System.out.println("commands:"+commands);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    @Test
    public  void test23(){
        videoCatchImg("D:\\video\\869903fb-5804-4d59-b42d-184efe83be0d.mp4",
                "D:\\ffmpeg\\ffmpeg-20200601-dd76226-win64-static\\bin\\ffmpeg.exe");

    }



}
