package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Fendback;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * <p>
 * 反馈表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface IFendbackService extends IService<Fendback> {

    List<Fendback> selectFeedback();


    Fendback selectFeedbackByFeedbackId(String feedbackid);

    void deleteFeedback(String feedbackid);
}
