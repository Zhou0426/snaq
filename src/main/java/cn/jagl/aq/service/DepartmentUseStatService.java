package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.DepartmentUseStat;

public interface DepartmentUseStatService {

	//��ҳ��ѯ
	List<DepartmentUseStat> findByPage(String hql , int pageNo, int pageSize);

	long countHql(String hql);
}
