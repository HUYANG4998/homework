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
 * 订单详情
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orderinfo")
public class Orderinfo extends Model<Orderinfo> {

    private static final long serialVersionUID = 1L;

    public Orderinfo(){
        String s=Utils.getUUID();;
        orderinfoid= s.substring(0,s.length() - 2);
    }
    @TableId("orderinfoid")
    private String orderinfoid;

    private String addtime;

    private String orderid;

    /**
     * main/deputy
     */
    private String cartype;

    private String carid;

    private Double price;

    private String carcolor;

    private String userid;

    /**
     * 订单方支付状态
     */
    private String orderpaystatus;

    /**
     * 车主支付状态
     */
    private String carmainpaystatus;

    private String status;

    /**
     * 联系人
     */
    private String liaisonname;

    /**
     * 联系电话
     */
    private String liaisonphone;

    /**
     * 类型 0待支付1已结单/派单中2进行中3已完成
     * @return
     */
    private String type;

    /**
     * 审核状态 0待预定1已预定
     * @return
     */
    private String reviewstatus;

    /**
     * 保证金
     * @return
     */
    private Integer margin;

    private Double realprice;

    private String cause;

    private String is_sign;

    private String updatetime;

    private String settlement;

    private String time;

    @Override
    protected Serializable pkVal() {
        return this.orderinfoid;
    }

}
