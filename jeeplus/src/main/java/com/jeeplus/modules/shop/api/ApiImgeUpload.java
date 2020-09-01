package com.jeeplus.modules.shop.api;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.core.mapper.JsonMapper;
import com.jeeplus.core.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author lhh
 * @date 2019/7/5 0005
 */
@Controller
@ResponseBody
@CrossOrigin
@Api(value="ApiImgeUpload",description="App上传图片控制器")
@RequestMapping(value = "${adminPath}/api/image")
public class ApiImgeUpload extends BaseController {

    @ApiOperation(notes = "upload", httpMethod = "POST", value = "上传图片")
    @ApiImplicitParams({

    })
    @ResponseBody
    @RequestMapping(value = "/upload")
    public String upload(MultipartHttpServletRequest request, HttpServletResponse response)  throws Exception {
        String json = null;
        response.setCharacterEncoding("UTF-8");
        HashMap<Object, Object> result = new HashMap<>();
        try {
            String attUrl = null;
            Double Duration = 0.0;
            Double size = 0.0;
            String imgUrl=null;
            for (Iterator it=request.getFileNames(); it.hasNext();) {
                String key = (String)it.next();
                MultipartFile file = request.getFile(key);
                System.out.println(file.getSize());
                if(file.getOriginalFilename().length()>0) {
                    String originalFilename = file.getOriginalFilename();

                    String fileExt = 	originalFilename.substring(originalFilename.lastIndexOf(".")+1);
                    originalFilename = System.currentTimeMillis()+"."+fileExt;
                    System.out.println(originalFilename);

                    String realpath =Global.USERFILES_BASE_URL + "upload/" +DateUtils.getYear() +"/"+ DateUtils.getMonth() +"/"+DateUtils.getDay()+"/";
                    FileUtils.createDirectory(Global.getUserfilesBaseDir() +realpath);
                    File file2 = new File(Global.getUserfilesBaseDir() + realpath + originalFilename);
                    file.transferTo(file2);
                        attUrl ="/jeeplus"+ realpath + originalFilename;
                    if(fileExt.equals("MP4")||fileExt.equals("mp4")){
                        int mobile_code = (int)((Math.random()*9+1)*100000);
                        imgUrl = Global.getUserfilesBaseDir() + realpath+mobile_code+".jpg";
                        this.fetchFrame("http:///"+attUrl,imgUrl);
                    }

                }
                result.put("success", true);
                result.put("msg","上传成功");
                result.put("code","-1");
                result.put("AttUrl", attUrl);
                if(null!=imgUrl){
                    result.put("imgUrl", "/jeeplus"+imgUrl);
                }
                String jsonString = JsonMapper.toJsonString(result);
                System.err.println(jsonString);
                return jsonString;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return null;
    }


    public  void fetchFrame(String videofile, String framefile)
            throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int length = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < length) {
            // 去掉前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabImage();
            if ((i > 5) && (f.image != null)) {
                break;
            }
            i++;
        }

        System.out.println("xxx"+FrameToBufferedImage(f));
        ImageIO.write(FrameToBufferedImage(f), "jpg", targetFile);
        //ff.flush();
        ff.stop();
        System.err.println(System.currentTimeMillis() - start);
    }

    public static BufferedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter=new Java2DFrameConverter();
        BufferedImage bufferedImage=converter.getBufferedImage(frame);
        return bufferedImage;
    }

}
