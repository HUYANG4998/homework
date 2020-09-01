package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Help;
import com.wxtemplate.api.entity.SonHelp;
import com.wxtemplate.api.mapper.HelpMapper;
import com.wxtemplate.api.service.IHelpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 帮助中心 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-18
 */
@Service
public class HelpServiceImpl extends ServiceImpl<HelpMapper, Help> implements IHelpService {

    @Resource
    private HelpMapper helpMapper;

    @Override
    public List<Help> selectHelpStyle() {
        return helpMapper.selectHelpStyle();

    }

    @Override
    public void deletHelp(String helpId) {
        if (!StringUtils.isEmpty(helpId)) {
            helpMapper.deleteById(helpId);
            deleteSon(helpId);
        }
    }
    private void deleteSon(String helpId){
        List<String> list = helpMapper.selectByFirstId(helpId);
        if(list.size()>0){
            for (String help_id:list){
                helpMapper.deleteById(help_id);
                deleteSon(help_id);
            }
        }

    }

    @Override
    public Help selectHelpById(String helpId) {
        Help help = null;
        if (!StringUtils.isEmpty(helpId)) {
            help = helpMapper.selectById(helpId);
        }
        return help;
    }

    @Override
    public void addStyle(Help help) {
        if (help != null) {
            help.setAddtime(DateUtil.now());
            helpMapper.insert(help);
        }
    }

    @Override
    public void updateHelp(Help help) {
        if (help != null) {
            helpMapper.updateById(help);
        }
    }

    @Override
    public List<Map<String,Object>> selectViceHelp() {

        return helpMapper.selectViceHelpById();
    }

    @Override
    public List<Map<String,Object>> selectProHelp() {


        return helpMapper.selectProHelpById();
    }

    @Override
    public List<Map<String, Object>> selectStyleAllAndOne() {
        List<Map<String,Object>> list=new ArrayList<>();
        List<Help> helps = helpMapper.selectHelpStyle();
        boolean flag=true;
        for (Help h:helps){
            Map<String,Object> map=new HashMap<>();
            map.put("style",h);
            List<Help> helps1 = helpMapper.selectByFirstIdAll(h.getHelpId());
            map.put("vice",helps1);
            if(flag){
                List<Help> helps2 = helpMapper.selectByFirstIdAll(helps1.get(0).getHelpId());
                map.put("problem",helps2);

                flag=false;
            }
            list.add(map);

        }
        return list;
    }

    @Override
    public List<Help> selectViceProblems(String firstId) {
        List<Help> list=new ArrayList<>();
        if(!StringUtils.isEmpty(firstId)){
            list=helpMapper.selectByFirstIdAll(firstId);
        }
        return list;
    }
}
