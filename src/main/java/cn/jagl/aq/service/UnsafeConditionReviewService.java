package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeConditionReview;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:27:16
 */
public interface UnsafeConditionReviewService {
	//添加
	public void save(UnsafeConditionReview unsafeConditionReview);
	//查询
	public List<UnsafeConditionReview> query(int id);
}
