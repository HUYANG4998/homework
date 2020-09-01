package com.wxtemplate.api.util;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class SqlSessionFactoryUtils {
	private final static Logger logger = LoggerFactory.getLogger(SqlSessionFactoryUtils.class);
	private static SqlSessionFactory sqlSessionFactory;
	@Autowired
	private SqlSessionFactory sqlSessionFactoryBean;
	
	@PostConstruct
	public void init() throws Exception{
		sqlSessionFactory=sqlSessionFactoryBean;
		if(sqlSessionFactory!=null){
			logger.info("sqlSessionFactory 初始化成功");
		}else{
			logger.info("sqlSessionFactory 初始化失败！！！");
			throw new Exception("sqlSessionFactory 初始化失败！！！");
		}
    }
	
	public static SqlSessionFactory getSqlSessionFactory(){
		return sqlSessionFactory;
	}
	
}
