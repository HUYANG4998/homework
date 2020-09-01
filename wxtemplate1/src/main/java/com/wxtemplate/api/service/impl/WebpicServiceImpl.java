package com.wxtemplate.api.service.impl;

import com.wxtemplate.api.entity.Webpic;
import com.wxtemplate.api.mapper.WebpicMapper;
import com.wxtemplate.api.service.IWebpicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 网站三个大图 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-23
 */
@Service
public class WebpicServiceImpl extends ServiceImpl<WebpicMapper, Webpic> implements IWebpicService {

    @Resource
    private WebpicMapper webpicMapper;

    @Override
    public void addWebPic(Webpic webpic) {
        if(webpic!=null){
            webpicMapper.insert(webpic);
        }
    }

    @Override
    public void updateWebPic(Webpic webpic) {
            if(webpic!=null){
                webpicMapper.updateById(webpic);
            }
    }

    @Override
    public Webpic selectWebPic() {

        return webpicMapper.selectWebPic();
    }
}
