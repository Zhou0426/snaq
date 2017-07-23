package cn.jagl.aq.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SuperviseItemDao;
import cn.jagl.aq.domain.SuperviseItem;
@Repository("superviseItemDao")
@SuppressWarnings("unchecked")
public class SuperviseItemDaoImpl extends BaseDaoHibernate5<SuperviseItem>
	implements SuperviseItemDao {
	@Override
	public SuperviseItem getSuperviseItemBySn(String superviseItemSn) {
		String hql="select p from SuperviseItem p where p.superviseItemSn=:superviseItemSn";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("superviseItemSn",superviseItemSn);;
		return (SuperviseItem)query.uniqueResult();
	}


	
	@Override
	public List<SuperviseItem> getSuperviseItemsByType(String departmentTypeSn) {
		String hql="select p from SuperviseItem p where p.departmentType.departmentTypeSn=:departmentTypeSn";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("departmentTypeSn", departmentTypeSn);
		return (List<SuperviseItem>)query.list();
	}

	@Override
	public List<SuperviseItem> getSuperviseParentItemsByType(String departmentTypeSn) {
		String hql="select p from SuperviseItem p where p.departmentType.departmentTypeSn=:departmentTypeSn and p.parent is null";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql)
				.setString("departmentTypeSn", departmentTypeSn);
		return (List<SuperviseItem>)query.list();
	}
}
