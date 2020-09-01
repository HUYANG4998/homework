package com.jeeplus.modules.shop.api.pay;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;

import com.jeeplus.modules.shop.api.payutils.alipay.AlipayTranfer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author lhh
 * @date 2019/9/23 0023
 */
@Api(value="ApiTranferController",description="App提现控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/customer/")
public class ApiTranferController extends BaseController {


//    @Autowired
//    private CustomerService customerService;


    @ApiOperation(notes = "tranferMoney", httpMethod = "POST", value = "提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name="price",value = "金额",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="account",value = "支付宝账号|微信账号",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="type",value = "提现类型（0:支付宝 1:微信）",required = true, paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "String")
    })
    @ResponseBody
    @RequestMapping(value = "/tranferMoney")
    public AjaxJson initAliPay(HttpServletRequest request, HttpServletResponse response){
        AjaxJson j=new AjaxJson();

        String type=request.getParameter("type");

        if(type.equals("0")){
            //支付宝提现
            boolean i=AlipayTranfer.AlipayTransfer("", Double.valueOf(request.getParameter("price")), request.getParameter("account"));
            System.out.println("xxxxxxxxxxxxx"+i);
            if(i){
                //提现成功
//                String customer_id=request.getParameter("customer_id");
//
//                Customer customer=customerService.get(customer_id);
//                Double money=customer.getMoney();
//
//                Double price=Double.valueOf(request.getParameter("price"));
//
//                BigDecimal bigDecimal=new BigDecimal(money);
//                BigDecimal bigDecimal1=new BigDecimal(price);
//
//
//                BigDecimal subtract=bigDecimal.subtract(bigDecimal1);
//
//                double v=subtract.doubleValue();
//
//                customer.setMoney(v);
//                customerService.save(customer);

                j.setErrorCode("666");
                j.setSuccess(true);
                j.setMsg("提现成功");

            }else if(!i){
                //提现失败
                j.setErrorCode("777");
                j.setSuccess(false);
                j.setMsg("提现失败 参数有误");
            }
        }else if (type.equals("2")){
            j.setMsg("微信提现暂未开放");
            j.setSuccess(false);
        }

        return  j;
    }

}
