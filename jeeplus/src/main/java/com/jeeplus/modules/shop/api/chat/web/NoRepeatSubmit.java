package com.jeeplus.modules.shop.api.chat.web;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {
    /**
     * 重复统计时长
     * @return
     */
    int time() default 1;
}
