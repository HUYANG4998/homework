package com.wxtemplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    public PassportInterceptor passportInterceptor;

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        /* 是否允许请求带有验证信息 */
        corsConfiguration.setAllowCredentials(true);
        /* 允许访问的客户端域名 */
        corsConfiguration.addAllowedOrigin("*");
        /* 允许服务端访问的客户端请求头 */
        corsConfiguration.addAllowedHeader("*");
        /* 允许访问的方法名,GET POST等 */
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);


    }
    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:/usr/local/imgs/");
       //registry.addResourceHandler("/image/**").addResourceLocations("file:C:/Users/lv/Desktop/imglist/");
    }
    public void addInterceptors(InterceptorRegistry registry) {
        //excludePathPatterns("/users/user/login","/users/user/register","/users/user/Sms","/users/user/loginSms","/users/user/callBackWeiXinPay"
        registry.addInterceptor(passportInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/user/SignIn","/api/create","/api/user/register",
                "/api/user/forgetPassword","/api/user/code","/api/uploadImage","/api/officialcar/selectofficialcar","/api/officialcar/selectofficialcarByStatus",
                "/api/officialcar/selectofficialcarByCarId","/api/carousel/selectCarousel","/api/upfileImage","/api/enduser/SignIn","/api/enduser/register","/api/layimloadImage",
                "/api/package/selectPacckageFour","/api/violation/selectViolation","/api/package/selectPackageById","/api/package/selectPacckageFour","/api/package/selectWebPackage",
                "/api/officialcar/selectofficialcarComment","/api/help/selectViceProblems","/api/help/selectStyleAllAndOne","/api/promotebasic/selectBasic","/api/webpic/selectWebPic",
                "/api/download","/api/version/contrastVersion","/api/imgs/select"
        );
    }
}