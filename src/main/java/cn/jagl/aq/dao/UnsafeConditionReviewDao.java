package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeConditionReview;

/**
 * @author mahui
 * @method 
 * @date 2016��7��23������4:10:28
 */
public interface UnsafeConditionReviewDao extends BaseDao<UnsafeConditionReview> {
	//���ݲ�������ID��ѯ
	List<UnsafeConditionReview> query(int id);
}
