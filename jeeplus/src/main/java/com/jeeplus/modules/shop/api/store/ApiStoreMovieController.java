package com.jeeplus.modules.shop.api.store;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.customerassit.entity.CustomerAssit;
import com.jeeplus.modules.shop.customerassit.service.CustomerAssitService;
import com.jeeplus.modules.shop.customerfollow.entity.CustomerFollow;
import com.jeeplus.modules.shop.customerfollow.service.CustomerFollowService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.storemovie.service.StoreMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lhh
 * @date 2019/11/27 0027
 */
@Api(value="ApiStoreMovieController",description="App商家动态控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/storeMovie")
public class ApiStoreMovieController extends BaseController {
    @Autowired
    private StoreMovieService storeMovieService;
    @Autowired
    private StoreService storeService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAssitService customerAssitService;
    @Autowired
    private CustomerFollowService customerFollowService;
    @ModelAttribute
    public StoreMovie get(@RequestParam(required=false) String id) {
        StoreMovie entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = storeMovieService.get(id);
        }
        if (entity == null){
            entity = new StoreMovie();
        }
        return entity;
    }

    @ApiOperation(notes = "appGetStoreMovie", httpMethod = "GET", value = "获取商家动态信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageNum",value = "第几页",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="pageSize",value = "每页条数",required = true, paramType = "query",dataType = "string")

    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreMovie")
    public AjaxJson appGetStoreMovie(String store_id){
        AjaxJson j=new AjaxJson();
        Store store=storeService.get(store_id);


        StoreMovie storeMovie=new StoreMovie();

        storeMovie.setStore(store);
        Page<StoreMovie> list=storeMovieService.findPage(new Page<>(0,10),storeMovie);

        j.put("data",list);
        j.setSuccess(true);
        j.setMsg("查询成功");
        j.setErrorCode("666");
        return j;
    }
    @ApiOperation(notes = "getMovieDetail", httpMethod = "GET", value = "获取商家动态详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "动态id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/getMovieDetail")
    public AjaxJson getMovieDetail(String id){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(id)){
            StoreMovie storeMovie=storeMovieService.get(id);
            if(storeMovie!=null){
                j.put("data",storeMovie);
                j.setSuccess(true);
                j.setMsg("查询成功");
            }else{
                j.setSuccess(false);
                j.setMsg("动态信息不存在");
            }

        }else{
            j.setSuccess(false);
            j.setMsg("动态id为空");
        }

        return j;
    }


    @ApiOperation(notes = "appGetStoreMovieDetail", httpMethod = "GET", value = "获取商家动态详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "动态id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreMovieDetail")
    public AjaxJson appGetStoreMovieDetail(StoreMovie storeMovie,String customer_id){
        AjaxJson j=new AjaxJson();
        Customer customer=customerService.get(customer_id);
        //判断该视频该用户是否点赞
        CustomerAssit customerAssit=new CustomerAssit();
        customerAssit.setCustomer(customer);
        customerAssit.setMovie(storeMovie);
        List<CustomerAssit> list=customerAssitService.findList(customerAssit);
        if(list.size() == 0){
            storeMovie.setIsAssist("0");
        }else {
            storeMovie.setIsAssist("1");
        }
        //判断该商家该用户是否关注
        Store store=storeService.get(storeMovie.getStore().getId());
        CustomerFollow customerFollow=new CustomerFollow();
        customerFollow.setCustomer(customer);
        customerFollow.setStore(store);
        List<CustomerFollow> list1=customerFollowService.findList(customerFollow);
        if(list1.size() == 0){
            storeMovie.setIsFllow("0");
        }else {
            storeMovie.setIsFllow("1");
        }
        if (storeMovie.getTitle() == null){
            j.setSuccess(false);
            j.setMsg("暂无此动态数据");
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",storeMovie);
        }
        return j;
    }


    @ApiOperation(notes = "appAddStoreMovie", httpMethod = "GET", value = "添加商家动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="movie",value = "视频地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="movieImg",value = "封面图片地址",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="title",value = "标题",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appAddStoreMovie")
    public AjaxJson appAddStoreMovie(StoreMovie storeMovie,String store_id){
        AjaxJson j=new AjaxJson();


        if(null == store_id||store_id.equals("")){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("缺少商家id参数");
            return j;
        }

        storeMovie.setDianNum("0");
        storeMovie.setPingNum("0");
        storeMovie.setStore(storeService.get(store_id));
        storeMovieService.save(storeMovie);
        j.setMsg("发布动态成功");
        j.setSuccess(true);
        j.setErrorCode("666");



        return j;
    }


    @ApiOperation(notes = "appDeleteStoreMovie", httpMethod = "DELETE", value = "删除商家动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "动态id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appDeleteStoreMovie")
    public AjaxJson appDeleteStoreMovie(StoreMovie storeMovie){
        AjaxJson j=new AjaxJson();

        storeMovieService.delete(storeMovie);
        j.setSuccess(true);
        j.setMsg("删除成功");
        return j;
    }

}
