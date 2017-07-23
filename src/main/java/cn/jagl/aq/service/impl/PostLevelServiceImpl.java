package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.PostLevelDao;
import cn.jagl.aq.domain.Certification;
import cn.jagl.aq.domain.PostLevel;
import cn.jagl.aq.service.PostLevelService;

@Service("postLevelService")
public class PostLevelServiceImpl implements PostLevelService{
	@Resource(name="postLevelDao")
	private PostLevelDao postLevelDao;

	@Override
	public PostLevel getBySn(String levelSn) {
		return postLevelDao.getBySn(levelSn);
		
	}

	@Override
	public List<PostLevel> findByPage(String hql, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return postLevelDao.findByPage(hql, pageNo, pageSize);
	}

	@Override
	public long countHql(String hql) {
		// TODO Auto-generated method stub
		return postLevelDao.countHql(hql);
	}

}
