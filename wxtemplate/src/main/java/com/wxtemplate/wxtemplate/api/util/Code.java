package com.wxtemplate.wxtemplate.api.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.EnumMap;

public class Code {
    public static void main(String[] args){
        //decodeQR("http://119.45.187.154:8088/image/pilar666.png");
        String str="http://119.45.187.154:8088/image/pilar666.png";
        String substring = str.substring(str.lastIndexOf("/") + 1);
        System.out.println(substring);
    }

    /**
           * 解析二维码图片
           * @param filePath 图片路径
           */
      public static String decodeQR(String filePath) {
                if ("".equalsIgnoreCase(filePath) && filePath.length() == 0) {
                         return "二维码图片不存在!";
                     }
                 String content = "";
                 EnumMap<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
                 hints.put(DecodeHintType.CHARACTER_SET, "UTF-8"); // 指定编码方式,防止中文乱码
                try {
                       BufferedImage image = ImageIO.read(new FileInputStream(filePath));
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                         Binarizer binarizer = new HybridBinarizer(source);
                       BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
                         MultiFormatReader reader = new MultiFormatReader();
                         Result result = reader.decode(binaryBitmap, hints);
                        content = result.getText();
                     } catch (Exception e) {
                       e.printStackTrace();
                   }
                 return content;
             }
}
