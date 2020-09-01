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
 * 订单
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders extends Model<Orders> {

    private static final long serialVersionUID = 1L;
    public Orders(){
        super();
        String s=Utils.getUUID();;
        orderid= s.substring(0,s.length() - 2);
    }
    @TableId("orderid")
    private String orderid;

    private String userid;

    private String maincar;

    private String maincartype;

    private String maincarcolor;

    private Double maincarprice;

    private String deputycartype;

    private String deputycarcolor;

    private Double deputycarprice;

    private Integer deputycarnum;

    /**
     * 新娘地址
     */
    private String brideadress;

    private String bridejwd;
    /**
     * 新郎地址
     */
    private String groomadress;

    private String groomjwd;
    /**
     * 酒店地址
     */
    private String hoteladress;

    private String hoteljwd;

    /**
     * 接亲日期
     */
    private String marrytime;

    private String hour;

    /**
     * 公里
     */
    private String kilometre;

    /**
     * 联系人
     */
    private String liaison;

    /**
     * 联系人电话
     */
    private String liaisonphone;

    private String addtime;

    /**
     * 超时费用
     */
    private Double overtimeprice;

    /**
     * 0待接单1进行中2已完成
     */
    private String orderstatus;

    /**
     * 接亲时段
     */
    private String marrystage;

    private Double allprice;

    private String maincarid;

    private String deputycarid;

    /**
     * 审核状态 0待审核1通过2不通过
     * @return
     */
    private String status;

    /**
     * 驳回原因
     * @return
     */
    private String reason;

    private String remark;

    private String notice;

    private String ordernumber;

    private String decs;

    private String descr;

    private String iscomment;



    @Override
    protected Serializable pkVal() {
        return this.orderid;
    }

}
