package com.wxtemplate.api.service.impl;

import com.wxtemplate.api.entity.Brand;
import com.wxtemplate.api.mapper.BrandMapper;
import com.wxtemplate.api.service.IBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

}
