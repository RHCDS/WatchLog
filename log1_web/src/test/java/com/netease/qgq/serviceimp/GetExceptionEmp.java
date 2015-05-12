package com.netease.qgq.serviceimp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.meta.Exception;
import com.netease.qgq.service.GetException;

@Service("getException")
public class GetExceptionEmp implements GetException{

	@Resource
	private ExceptionDao exceptionDao;
	@Override
	public  Exception getException(int exceptionId) {
		// TODO Auto-generated method stub
		return this.exceptionDao.findByExceptionId(exceptionId);
	}

}
