package com.jeeplus.modules.shop.api.customer;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import com.jeeplus.modules.shop.customeraddress.service.CustomerAddressService;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.utils.DictUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author lhh
 * @date 2019/12/23 0002
 */
@Api(value="ApiCustomerController",description="App用户端收货地址控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/customer")
public class ApiCustomerAddressController extends BaseController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAddressService customerAddressService;
    @ModelAttribute
    public CustomerAddress get(@RequestParam(required=false) String id) {
        CustomerAddress entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = customerAddressService.get(id);
        }
        if (entity == null){
            entity = new CustomerAddress();
        }
        return entity;
    }


    @ApiOperation(notes = "appCustomerAddress", httpMethod = "POST", value = "用户添加收货地址/编辑收货地址(复合)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "收货人姓名",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="customerSex",value = "收货人性别",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="phone",value = "电话号码",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="address",value = "地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="lng",value = "经度",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="lat",value = "纬度",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="addressDetail",value = "详细地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="yesNo",value = "是否默认",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="addressType",value = "地址类别",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0:新增1:编辑",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="id",value = "地址id(只有编辑的时候传)",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appCustomerAddress")
    public AjaxJson appCustomerAddress(CustomerAddress customerAddress, String type,String customer_id){
        AjaxJson j=new AjaxJson();
        Customer customer=customerService.get(customer_id);
        if(customer==null){
            j.setSuccess(false);
            j.setMsg("此用户不存在");
            j.setErrorCode("111");
            return j;
        }
        customerAddress.setCustomer(customer);
        if(type.equals("") || null == type){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        if(!StringUtils.isEmpty(customerAddress.getYesNo())){
            if(customerAddress.getYesNo().equals("1")){
                CustomerAddress customerAddress1=new CustomerAddress();
                customerAddress1.setYesNo("1");
                List<CustomerAddress> list=customerAddressService.findList(customerAddress1);
                if(list.size()>=1){
                    //修改成为0不默认
                    for (CustomerAddress c:
                            list) {
                        c.setYesNo("0");
                        customerAddressService.save(c);
                    }
                }
            }
        }

        if(type.equals("0")){
            //新增
            /*customerAddress.setYesNo("0");*/
            customerAddressService.save(customerAddress);
            j.setSuccess(true);
            j.setMsg("新增地址成功");
            j.setErrorCode("666");
        }else if(type.equals("1")){
            //修改
            customerAddressService.save(customerAddress);
            j.setMsg("修改地址成功");
            j.setErrorCode("666");
            j.setSuccess(true);
        }
        return j;
    }

    @ApiOperation(notes = "appGetDict", httpMethod = "GET", value = "获取字典值收货人性别/地址类型(复合)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value = "0:查询收货人性别1:查询地址类型",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetDict")
    public AjaxJson appGetDict(String type){
        AjaxJson j=new AjaxJson();
        if(type.equals("") || null == type){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }

        if(type.equals("0")){
            //查询性别
            List<DictValue> list=DictUtils.getDictList("customer_sex");
            j.put("data",list);
        }else if(type.equals("1")){
            //查询地址类型
            List<DictValue> list=DictUtils.getDictList("address_type");
            j.put("data",list);
        }
        j.setMsg("查询成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        return j;
    }


    @ApiOperation(notes = "appGetCustomerAddress", httpMethod = "GET", value = "获取我的收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetCustomerAddress")
    public AjaxJson appCustomerAssit(String customer_id){
        AjaxJson j=new AjaxJson();
        if(customer_id.equals("") || null == customer_id){
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
            return j;
        }
        Customer customer=customerService.get(customer_id);

        CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setCustomer(customer);
        customerAddress.setYesNo("1");
        List<CustomerAddress> list=customerAddressService.findList(customerAddress);
        customerAddress.setYesNo("0");
        list.addAll(customerAddressService.findList(customerAddress));
        j.setMsg("查询成功");
        j.setSuccess(true);
        j.put("data",list);
        return j;
    }

//    public String values(String type,String value){
//        List<DictValue> dictList=DictUtils.getDictList(type);
//        String values = "";
//        for (int i=0; i <dictList.size() ; i++) {
//           DictValue d =  dictList.get(i);
//           if(d.getValue() == value){
//               values= d.getValue();
//           }
//        }
//        return values;
//    }
    @ApiOperation(notes = "appDeleteCustomerAddress", httpMethod = "DELETE", value = "删除我的收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "地址",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appDeleteCustomerAddress")
    public AjaxJson appDeleteCustomerAddress(CustomerAddress customerAddress){
        AjaxJson j=new AjaxJson();
        customerAddressService.delete(customerAddress);
        j.setErrorCode("666");
        j.setSuccess(true);
        j.setMsg("删除成功");
        return j;
    }
}
