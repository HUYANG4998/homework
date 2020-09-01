package com.jeeplus.modules.shop.api.store;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customer.service.CustomerService;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.storemovie.service.StoreMovieService;
import com.jeeplus.modules.shop.storemoviereply.entity.Node;
import com.jeeplus.modules.shop.storemoviereply.entity.StoreMovieReply;
import com.jeeplus.modules.shop.storemoviereply.mapper.StoreMovieReplyMapper;
import com.jeeplus.modules.shop.storemoviereply.service.StoreMovieReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lhh
 * @date 2019/11/27 0027
 */
@Api(value="ApiStoreMovieController",description="App商家动态评论控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/storeMovieReply")
public class ApiStoreMovieReplyController extends BaseController {
    @Autowired
    private StoreMovieService storeMovieService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreMovieReplyService storeMovieReplyService;
    @Autowired
    private CustomerService customerService;

    @ApiOperation(notes = "appGetStoreMovieReply", httpMethod = "GET", value = "获取商家动态评论详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="movie_id",value = "动态id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetStoreMovieDetail")
    public AjaxJson appGetStoreMovieDetail(HttpServletRequest request){
        AjaxJson j=new AjaxJson( );
        String id=request.getParameter("movie_id");

        if(null == id || "".equals(id)){
            j.setMsg("动态id异常");
            j.setErrorCode("111");
            j.setSuccess(false);
            return j;
        }
        //根据id查询出动态对象
        StoreMovie storeMovie=storeMovieService.get(id);
        StoreMovieReply storeMovieReply=new StoreMovieReply();
        storeMovieReply.setMovie(storeMovie);
        //查询此动态且lastId为null的集合
        List<StoreMovieReply> firstList =storeMovieReplyService.findAllByPropertyIdAndLastIdNull(storeMovieReply);
        //查询此动态且lastId不为null的集合
        List<StoreMovieReply> thenList =storeMovieReplyService.findAllByPropertyIdAndLastIdNotNull(storeMovieReply);
        //新建一个Node集合。
        ArrayList<Node> nodes=new ArrayList<>();
        //将第一层评论都添加都Node集合中
        for (StoreMovieReply s:
             firstList) {
            nodes.add(new Node(s) );
        }
        //将回复添加到对应的位置
        List list = Node.addAllNode(nodes, thenList);
        if(list.size() == 0){
            j.setSuccess(false);
            j.setMsg("暂无数据");
            j.setErrorCode("1111");
        }else {
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",list);
        }

        return j;
    }


    @ApiOperation(notes = "appAddStoreMovieReply", httpMethod = "POST", value = "评论商家动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="movie_id",value = "动态id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0商家1用户",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="msg",value = "内容",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appAddStoreMovieReply")
    public AjaxJson appAddStoreMovieReply(String movie_id,String customer_id,String type,String store_id,String msg){

        AjaxJson j=new AjaxJson();

        if(null == movie_id || "".equals(movie_id)  || null ==msg || "".equals(msg) ){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("参数错误");
            return j;
        }

        StoreMovie storeMovie=storeMovieService.get(movie_id);
        if(null == storeMovie){
            j.setMsg("此动态不存在");
            j.setSuccess(false);
            j.setErrorCode("111");
            return j;
        }

        Customer customer=customerService.get(customer_id);
        Store store=storeService.get(store_id);
//        if(null == customer){
//            j.setMsg("此用户不存在");
//            j.setSuccess(false);
//            j.setErrorCode("111");
//            return j;
//        }


        StoreMovieReply storeMovieReply=new StoreMovieReply();
        storeMovieReply.setMovie(storeMovie);
        storeMovieReply.setCustomer(customer);
        storeMovieReply.setStore(store);
        storeMovieReply.setRemarks(msg);
        storeMovieReply.setYesNo(type);
        storeMovieReplyService.save(storeMovieReply);

        //动态评论次数加1
        int num=Integer.parseInt(storeMovie.getPingNum())+1;
        storeMovie.setPingNum(String.valueOf(num));
        storeMovieService.save(storeMovie);
        //end
        j.setErrorCode("666");
        j.setMsg("评论成功");
        j.setSuccess(true);
        return j;
    }


    @ApiOperation(notes = "appInsertStoreMovieReply", httpMethod = "POST", value = "回复商家动态评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="reply_id",value = "评论id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="movie_id",value = "动态id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="customer_id",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="store_id",value = "商家id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="type",value = "0商家1用户",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="msg",value = "内容",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appInsertStoreMovieReply")
    public AjaxJson appInsertStoreMovieReply(String reply_id,String movie_id,String customer_id,String type,String store_id,String msg){
        AjaxJson j=new AjaxJson();

        if( null == movie_id || "".equals(movie_id) ||null == reply_id || "".equals(reply_id)  || null ==msg || "".equals(msg) ){
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("参数错误");
            return j;
        }

        StoreMovie storeMovie=storeMovieService.get(movie_id);
        if(null == storeMovie){
            j.setMsg("此动态不存在");
            j.setSuccess(false);
            j.setErrorCode("111");
            return j;
        }

        Customer customer=customerService.get(customer_id);
        Store store=storeService.get(store_id);


        StoreMovieReply storeMovieReply=new StoreMovieReply();
        storeMovieReply.setRemarks(msg);
        storeMovieReply.setCustomer(customer);
        storeMovieReply.setMovie(storeMovie);
        storeMovieReply.setLastId(reply_id);
        storeMovieReply.setStore(store);
        storeMovieReply.setYesNo(type);
        storeMovieReplyService.save(storeMovieReply);
        j.setSuccess(true);
        j.setMsg("回复成功");
        j.setErrorCode("666");

        return j;

    }
    @ApiOperation(notes = "getCommentBack", httpMethod = "POST", value = "查看我所有评论下的所有回复")

    @ResponseBody
    @RequestMapping(value = "/getCommentBack")
    public AjaxJson getCommentBack(HttpServletRequest request){
        /*String userid= (String)request.getAttribute("userid");*/
        String userid="9ca3a0cbbd7744ad9fa288c10f895277";
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(userid)){
            StoreMovieReply storeMovieReply=new StoreMovieReply();
            storeMovieReply.setCustomer(customerService.get(userid));
            List<StoreMovieReply> replyList = storeMovieReplyService.findAllList(storeMovieReply);
            List<StoreMovieReply> list=new ArrayList<>();
            for (StoreMovieReply reply:replyList){
                StoreMovieReply r=new StoreMovieReply();
                r.setLastId(reply.getId());
                list.addAll(storeMovieReplyService.findList(r));
            }
            j.setSuccess(true);
            j.put("data",list);
        }else{
            j.setSuccess(false);
            j.setMsg("请登录");
        }
        return j;

    }






}
