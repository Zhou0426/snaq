package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.ManagementReviewDao;
import cn.jagl.aq.domain.ManagementReview;
import cn.jagl.aq.service.ManagementReviewService;
@Service("managementReviewService")
public class ManagementReviewServiceImpl implements ManagementReviewService{
	@Resource(name="managementReviewDao")
	private ManagementReviewDao managementReviewDao;

	@Override
	public long countHql(String hql) {
		return managementReviewDao.countHql(hql);
	}

	@Override
	public List<ManagementReview> findByPage(String hql, int pageNo, int pageSize) {
		return managementReviewDao.findByPage(hql, pageNo, pageSize);
	}

	@Override
	public void add(ManagementReview managementReview) {
		managementReviewDao.save(managementReview);
		
	}

	@Override
	public void delete(ManagementReview managementReview) {
		managementReviewDao.delete(managementReview);
		
	}

	@Override
	public void update(ManagementReview managementReview) {
		managementReviewDao.update(managementReview);
		
	}

	@Override
	public ManagementReview getById(int id) {
		return 	managementReviewDao.getById(id);
	}

	@Override
	public ManagementReview getBySn(String reviewSn) {
		// TODO Auto-generated method stub
		return managementReviewDao.getBySn(reviewSn);
	}
}
