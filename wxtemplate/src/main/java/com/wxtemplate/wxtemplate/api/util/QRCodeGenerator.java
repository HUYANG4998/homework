package com.wxtemplate.wxtemplate.api.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成
 */
public class QRCodeGenerator {
    /**
     *二维码实现
     * @param msg
     * @param path
     */
    public static void getBarCode(String msg,String path){
        try {
            File file=new File(path);
            OutputStream ous=new FileOutputStream(file);
            if(StringUtils.isEmpty(msg) || ous==null)
                return;
            String format = "png";
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType,String> map =new HashMap<EncodeHintType, String>();
            //设置编码 EncodeHintType类中可以设置MAX_SIZE， ERROR_CORRECTION，CHARACTER_SET，DATA_MATRIX_SHAPE，AZTEC_LAYERS等参数
            map.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            map.put(EncodeHintType.MARGIN,"2");
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(msg, BarcodeFormat.QR_CODE,300,300,map);
            MatrixToImageWriter.writeToStream(bitMatrix,format,ous);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        String msg = "2f289e0d854f4168b1eeb7c4de8709fa,0";
        String path = "C:/Users/lv/Desktop/imglist/pilar6661.png";
        QRCodeGenerator.getBarCode(msg,path);
    }

}
