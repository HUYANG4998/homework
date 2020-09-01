package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.UserRead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户读公告表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
public interface IUserReadService extends IService<UserRead> {

    List<Map<String, Object>> myNotice(String userId);

    void updateNotice(String userReadId);
}
