package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Asset;
import com.wxtemplate.api.entity.Earn;
import com.wxtemplate.api.entity.Notice;
import com.wxtemplate.api.entity.User;
import com.wxtemplate.api.mapper.AssetMapper;
import com.wxtemplate.api.mapper.EarnMapper;
import com.wxtemplate.api.mapper.NoticeMapper;
import com.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.api.service.IAssetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.api.util.PushtoSingle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-06
 */
@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements IAssetService {

    @Resource
    private AssetMapper assetMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private EarnMapper earnMapper;
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public List<Map<String, Object>> selectAssetToAudit(String cate) {
        List<Map<String,Object>> listMap=assetMapper.selectAssetToAudit(cate);
        listMap.parallelStream().forEach((map)->{
            if(map.containsKey("status")){
                String status=map.get("status").toString();
                if("0".equals(status)){
                    map.put("status","待审核");
                }else if("1".equals(status)){
                    map.put("status","已通过");
                }else{
                    map.put("status","已驳回");
                }
            }
        });
        return listMap;
    }

    @Override
    public List<Map<String, Object>> selectAssetRecord(String cate) {
        List<Map<String,Object>> listMap=assetMapper.selectAssetRecord(cate);
        listMap.parallelStream().forEach((map)->{
            if(map.containsKey("status")){
                String status=map.get("status").toString();
                if("0".equals(status)){
                    map.put("status","待审核");
                }else if("1".equals(status)){
                    map.put("status","已通过");
                }else{
                    map.put("status","已驳回");
                }
            }
        });
        return listMap;
    }

    @Override
    public Map<String, Object> selectAssetByAssetId(String assetid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(assetid)){
            map=assetMapper.selectAssetByAssetId(assetid);
        }
        return map;
    }

    @Override
    public void topUpAudit(Map<String, Object> result) {
        if(!result.isEmpty()){
            String assetid=result.get("assetid")==null?null:result.get("assetid").toString();
            String userid=result.get("userid")==null?null:result.get("userid").toString();
            Double real_price=result.get("real_price")==null?null:Double.valueOf(result.get("real_price").toString());
            String status=result.get("status")==null?null:result.get("status").toString();
            Asset asset =new Asset();
            asset.setAssetid(assetid);
            asset.setRealPrice(real_price);
            if(!StringUtils.isEmpty(status)){
                asset.setStatus(status);
                if("2".equals(status)) {
                    String cause = result.get("cause") == null ? null : result.get("cause").toString();
                    asset.setCause(cause);
                }else if("1".equals(status)){
                    if(!StringUtils.isEmpty(userid)){
                        User user=userMapper.selectById(userid);
                        user.setPrice(user.getPrice()+real_price);
                        userMapper.updateById(user);
                        Earn earn=new Earn("充值",real_price,userid,"0",DateUtil.now());
                        earnMapper.insert(earn);
                    }
                }
            }
            asset.setUpdatetime(DateUtil.now());
            assetMapper.updateById(asset);

        }
    }

    @Override
    public void withdrawalAudit(Map<String, Object> result) {
        if(!result.isEmpty()){
            String assetid=result.get("assetid")==null?null:result.get("assetid").toString();
            String userid=result.get("userid")==null?null:result.get("userid").toString();
            Double price=result.get("price")==null?null:Double.valueOf(result.get("price").toString());
            String status=result.get("status")==null?null:result.get("status").toString();
            Asset asset =new Asset();
            asset.setAssetid(assetid);
            if(!StringUtils.isEmpty(status)){
                asset.setStatus(status);
                if("2".equals(status)) {
                    String cause = result.get("cause") == null ? null : result.get("cause").toString();
                    asset.setCause(cause);
                    User user=userMapper.selectById(userid);
                    user.setPrice(user.getPrice()+price);
                    userMapper.updateById(user);
                    Earn earn=new Earn("提现驳回",price,user.getUserid(),"0",DateUtil.now());
                    earnMapper.insert(earn);
                    Notice notice=new Notice();
                    notice.setAddtime(DateUtil.now());
                    notice.setAddition("提现驳回");
                    notice.setContent("您提现已被驳回，请查看钱包退还金额是否正确！");
                    if(!StringUtils.isEmpty(user.getCid())){
                        Map<String,Object> map=new HashMap<>();
                        map.put("cid",user.getCid());
                        map.put("title",notice.getAddition());
                        map.put("text",notice.getContent());
                        map.put("version", user.getVersion());
                        PushtoSingle.push(map);
                    }

                    notice.setIsread("0");
                    notice.setUserid(userid);
                    noticeMapper.insert(notice);
                }else if("1".equals(status)){
                    if(!StringUtils.isEmpty(userid)){
                        Notice notice=new Notice();
                        notice.setAddtime(DateUtil.now());
                        notice.setAddition("提现到账");
                        notice.setContent("您已提现到账，请根据支付方式查看账号余额");

                        User infoUser=userMapper.selectById(userid);
                        if(!StringUtils.isEmpty(infoUser.getCid())){
                            Map<String,Object> map=new HashMap<>();
                            map.put("cid",infoUser.getCid());
                            map.put("title",notice.getAddition());
                            map.put("text",notice.getContent());
                            map.put("version", infoUser.getVersion());
                            PushtoSingle.push(map);
                        }

                        notice.setIsread("0");
                        notice.setUserid(userid);
                        noticeMapper.insert(notice);
                    }
                }
            }
            asset.setUpdatetime(DateUtil.now());
            assetMapper.updateById(asset);

        }
    }
}
