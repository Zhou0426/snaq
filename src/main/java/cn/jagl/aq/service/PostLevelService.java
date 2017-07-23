package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.domain.PostLevel;

public interface PostLevelService {
	PostLevel getBySn(String levelSn);

	//∑÷“≥≤È—Ø
	List<PostLevel> findByPage(String hql , int pageNo, int pageSize);

	long countHql(String hql);
}
