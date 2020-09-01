package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.entity.DynamicCollection;
import com.wxtemplate.wxtemplate.api.mapper.DynamicCollectionMapper;
import com.wxtemplate.wxtemplate.api.mapper.DynamicMapper;
import com.wxtemplate.wxtemplate.api.service.IDynamicCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * <p>
 * 动态点赞/收藏 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class DynamicCollectionServiceImpl extends ServiceImpl<DynamicCollectionMapper, DynamicCollection> implements IDynamicCollectionService {

    @Resource
    private DynamicCollectionMapper dynamicCollectionMapper;
    @Resource
    private DynamicMapper dynamicMapper;

    @Override
    public void insertDynamicCollection(DynamicCollection dynamicCollection) {
        if(dynamicCollection!=null){
            dynamicCollection.setAddtime(DateUtil.now());
            dynamicCollectionMapper.insert(dynamicCollection);
            if(!StringUtils.isEmpty(dynamicCollection.getType())){
                Dynamic dynamic = dynamicMapper.selectById(dynamicCollection.getDynamicId());
                if(dynamic!=null){
                    if("0".equals(dynamicCollection.getType())){
                        //点赞
                        dynamic.setGiveLikeNumber(dynamic.getGiveLikeNumber()+1);
                    }else{
                        //收藏
                        dynamic.setCollectNumber(dynamic.getCollectNumber()+1);
                    }
                    dynamicMapper.updateById(dynamic);
                }


            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDynamicCollection(DynamicCollection dynamicCollection) {
        if(dynamicCollection!=null){
            QueryWrapper qw = new QueryWrapper<DynamicCollection>();
            qw.eq("user_id",dynamicCollection.getUserId());
            qw.eq("dynamic_id",dynamicCollection.getDynamicId());
            qw.eq("type",dynamicCollection.getType());
            Optional<DynamicCollection> dynamicCollection1 = Optional.ofNullable(dynamicCollectionMapper.selectOne(qw));
            DynamicCollection dynamicCollection2 = dynamicCollection1.get();
            if(dynamicCollection2!=null){

            /*DynamicCollection dynamicCollection = dynamicCollectionMapper.selectById(dynamicCollection);*/

                String type = dynamicCollection.getType();
                if(!StringUtils.isEmpty(type)&&!StringUtils.isEmpty(dynamicCollection.getDynamicId())){
                    Dynamic dynamic = dynamicMapper.selectById(dynamicCollection.getDynamicId());
                    if(dynamic!=null){
                        if("0".equals(type)){
                            //取消点赞
                            dynamic.setGiveLikeNumber(dynamic.getGiveLikeNumber()-1);
                        }else{
                            //取消收藏
                            dynamic.setCollectNumber(dynamic.getCollectNumber()-1);
                        }
                        dynamicMapper.updateById(dynamic);
                    }

                }
                dynamicCollectionMapper.deleteById(dynamicCollection2.getDynamicCollectionId());

            }
        }
    }
}
