package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Realname;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实名 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Mapper
public interface RealnameMapper extends BaseMapper<Realname> {

    /**获取个人认证信息*/
    Map<String,Object> selectRealNameByUserId(String userid);

    List<Map<String, Object>> selectAllRealName(@Param("realname") String realname);

    Map<String, Object> selectRealnameByRealnameId(String realnameid);

    Realname selectRealNameByIdCard(String idcard);

    Realname selectRealByUserId(@Param("userid") String userid);
}
