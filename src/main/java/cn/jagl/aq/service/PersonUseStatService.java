package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.PersonUseStat;

public interface PersonUseStatService {

	//∑÷“≥≤È—Ø
	List<PersonUseStat> findByPage(String hql , int pageNo, int pageSize);

	long countHql(String hql);
}
