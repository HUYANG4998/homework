package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Webpic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站三个大图 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-23
 */
public interface IWebpicService extends IService<Webpic> {

    void addWebPic(Webpic webpic);

    void updateWebPic(Webpic webpic);

    Webpic selectWebPic();
}
