package cn.jagl.aq.dao.impl;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.NearMissTypeDao;
import cn.jagl.aq.domain.NearMissType;

@Repository("nearMissTypeDao")
public class NearMissTypeDaoImpl extends BaseDaoHibernate5<NearMissType>
implements NearMissTypeDao{

	public NearMissType getByNearMissTypeSn(String nearMissTypeSn){
		Query query =getSessionFactory().getCurrentSession()
		.createQuery("select n from NearMissType n where nearMissTypeSn=:nearMissTypeSn").setString("nearMissTypeSn", nearMissTypeSn);
		return (NearMissType)query.uniqueResult();
	}

	@Override
	public List<NearMissType> getAllNearMissType() {
		Query query =getSessionFactory().getCurrentSession()
		.createQuery("select n from NearMissType n");
		return (List<NearMissType>)query.list();
	};
}
