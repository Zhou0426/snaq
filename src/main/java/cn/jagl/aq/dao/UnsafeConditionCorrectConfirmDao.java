package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;

/**
 * @author mahui
 * @method 
 * @date 2016��7��23������4:05:19
 */
public interface UnsafeConditionCorrectConfirmDao extends BaseDao<UnsafeConditionCorrectConfirm> {
	//���ݲ�������id��ѯ
	List<UnsafeConditionCorrectConfirm> query(int id);
}
