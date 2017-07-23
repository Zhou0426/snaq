package cn.jagl.aq.dao.impl;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.UnsafeConditionCorrectConfirmDao;
import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;
/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:18:55
 */
@Repository("unsafeConditionCorrectConfirmDao")
@SuppressWarnings("unchecked")
public class UnsafeConditionCorrectConfirmDaoImpl extends BaseDaoHibernate5<UnsafeConditionCorrectConfirm>
		implements UnsafeConditionCorrectConfirmDao {

	@Override
	public List<UnsafeConditionCorrectConfirm> query(int id) {
		String hql="select i from UnsafeConditionCorrectConfirm i WHERE i.inconformityItem.id=:id";
		List<UnsafeConditionCorrectConfirm> list=getSessionFactory().getCurrentSession().createQuery(hql)
			.setParameter("id",id)
			.list();
		return list;
	}
	

}
