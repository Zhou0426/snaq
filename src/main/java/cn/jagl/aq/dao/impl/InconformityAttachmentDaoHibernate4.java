package cn.jagl.aq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.InconformityAttachmentDao;
import cn.jagl.aq.domain.InconformityAttachment;
/**
 * 
 * @author mahui
 *
 * @date 2016年7月8日
 */
@SuppressWarnings("unchecked")
@Repository("inconformityAttachmentDao")
public class InconformityAttachmentDaoHibernate4 extends BaseDaoHibernate5<InconformityAttachment> implements InconformityAttachmentDao {
	//查询总记录数
	@Override
	public long count(String hql) {
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql)
					.uniqueResult();
	}

	//多条删
	@Override
	public void deleteByIds(String ids) {
		String hql="update InconformityAttachment i SET i.deleted=true,i.inconformityItem=null where i.id in("+ids+")";
		getSessionFactory().getCurrentSession().createQuery(hql).executeUpdate();
	}
	
	//通过不符合项编号获取相关附件信息
	@Override
	public List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn) {
		String hql="select i FROM InconformityAttachment i WHERE i.deleted=false AND i.inconformityItem.inconformityItemSn=:inconformityItemSn";
		List<InconformityAttachment> list=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("inconformityItemSn", inconformityItemSn)
				.list();
		return list;
	}

}
