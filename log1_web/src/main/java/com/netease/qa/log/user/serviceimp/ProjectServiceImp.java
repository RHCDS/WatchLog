package com.netease.qa.log.user.serviceimp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.user.service.ProjectService;

@Service
public class ProjectServiceImp implements ProjectService {

	@Resource
	private ProjectDao projectDao;
	
	@Override
	public int addProject(String name, String name_eng, int accuracy) {
		// TODO Auto-generated method stub
		Project project = projectDao.findByName(name_eng);
		if(project != null){
			System.out.println("该项目已经存在，不能创建！");
			return project.getProjectId();
		}
		Project newProject = new Project();
		newProject.setProjectName(name);
		newProject.setProjectEngName(name_eng);
		newProject.setTimeAccuracy(accuracy);
		projectDao.insert(newProject);
		return newProject.getProjectId();
	}

	@Override
	public int updateProject(int projectid, String name, String name_eng,
			int accuracy) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Project findProject(int projectid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateProjectStatus(int projectid, int status) {
		// TODO Auto-generated method stub
		return 0;
	}

}
