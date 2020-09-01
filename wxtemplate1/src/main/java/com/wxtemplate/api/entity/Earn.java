package com.wxtemplate.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import com.wxtemplate.api.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收益表
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("earn")
public class Earn extends Model<Earn> {

    private static final long serialVersionUID = 1L;

    public Earn(String content,Double price,String userid,String earn,String addtime){

        earndetailid= Utils.getUUID();
        this.content=content;
        this.price=price;
        this.userid=userid;
        this.earn=earn;
        this.addtime=addtime;

    }
    public Earn(){

    }
    /**
     * 收益明细id
     */
    @TableId("earndetailid")
    private String earndetailid;

    /**
     * 收益内容
     */
    private String content;

    /**
     * 金额
     */
    private Double price;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 加0减1
     */
    private String earn;

    /**
     * 添加时间
     */
    private String addtime;


    @Override
    protected Serializable pkVal() {
        return this.earndetailid;
    }

}
