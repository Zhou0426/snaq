package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeConditionReview;

/**
 * @author mahui
 * @method 
 * @date 2016��7��23������4:27:16
 */
public interface UnsafeConditionReviewService {
	//���
	public void save(UnsafeConditionReview unsafeConditionReview);
	//��ѯ
	public List<UnsafeConditionReview> query(int id);
}
