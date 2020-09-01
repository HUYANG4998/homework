package com.wxtemplate.api.entity.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 车主认证
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OwnerCertVo{
    private static final long serialVersionUID = 1L;

    /**用户id*/
    private String userid;
    /**token*/
    private String token;
    /**实名姓名*/
    private String realname;
    /**身份证号*/
    private String idcard;
    /**身份证图片*/
    private String idcardimages;

}
