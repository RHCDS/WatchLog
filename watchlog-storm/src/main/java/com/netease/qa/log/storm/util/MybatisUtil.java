package com.netease.qa.log.storm.util;

import java.io.Reader;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.utils.Utils;



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
        Map env = Utils.readStormConfig();
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, ConfigReader.MYBATIS_ENV);
		logger.info("---init sqlSession factory---");
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

}