package com.wxtemplate.wxtemplate.api.controller;


import com.wxtemplate.wxtemplate.api.entity.MoneyTemplate;
import com.wxtemplate.wxtemplate.api.service.IMoneyTemplateService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 金钱模板表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/money-template")
@CrossOrigin
public class MoneyTemplateController {

    @Autowired
    private IMoneyTemplateService moneyTemplateService;

    /**
     * 查询价钱表
     * @return
     */
    @PostMapping("/selectMoneyTemplate")
    public Result selectMoneyTemplate() {
            MoneyTemplate moneyTemplate=moneyTemplateService.selectMoneyTemplate();
            return Result.success(moneyTemplate);
    }

    /**
     * 添加价钱表
     * @return
     */
    @PostMapping("/addMoneyTemplate")
    public Result addMoneyTemplate(MoneyTemplate moneyTemplate) {
        if(moneyTemplate !=null){
            moneyTemplateService.addMoneyTemplate(moneyTemplate);
            return Result.success("添加成功");
        }else{
            return Result.fail("添加失败");
        }

    }

    /**
     * 修改价钱表
     * @return
     */
    @PostMapping("/updateMoneyTemplate")
    public Result updateMoneyTemplate(MoneyTemplate moneyTemplate) {
        if(moneyTemplate !=null){
            moneyTemplateService.updateMoneyTemplate(moneyTemplate);
            return Result.success("修改成功");
        }else{
            return Result.fail("修改失败");
        }
    }

}
