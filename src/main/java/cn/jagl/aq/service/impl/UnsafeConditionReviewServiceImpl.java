package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeConditionReviewDao;
import cn.jagl.aq.domain.UnsafeConditionReview;

/**
 * @author mahui
 * @method 
 * @date 2016��7��23������4:34:05
 */
@Service("unsafeConditionReviewService")
public class UnsafeConditionReviewServiceImpl implements cn.jagl.aq.service.UnsafeConditionReviewService {
	@Resource(name="unsafeConditionReviewDao")
	private UnsafeConditionReviewDao unsafeConditionReviewDao;
	@Override
	public void save(UnsafeConditionReview unsafeConditionReview) {
		unsafeConditionReviewDao.save(unsafeConditionReview);
	}

	@Override
	public List<UnsafeConditionReview> query(int id) {
		return unsafeConditionReviewDao.query(id);
	}

}
