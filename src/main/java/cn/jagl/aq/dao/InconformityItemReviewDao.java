package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeConditionReview;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:10:28
 */
public interface InconformityItemReviewDao extends BaseDao<UnsafeConditionReview> {
	//根据不符合项ID查询
	List<UnsafeConditionReview> query(int id);
}
