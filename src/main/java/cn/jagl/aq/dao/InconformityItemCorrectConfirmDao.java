package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:05:19
 */
public interface InconformityItemCorrectConfirmDao extends BaseDao<UnsafeConditionCorrectConfirm> {
	//根据不符合项id查询
	List<UnsafeConditionCorrectConfirm> query(int id);
}
