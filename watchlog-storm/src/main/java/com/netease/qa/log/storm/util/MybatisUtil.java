package com.netease.qa.log.storm.util;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MybatisUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MybatisUtil.class);

    private final static SqlSessionFactory sqlSessionFactory;
    
    static {
        String resource = "mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (Exception e) {
        	logger.error("error", e);
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		logger.info("---init sqlSession factory---");
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}