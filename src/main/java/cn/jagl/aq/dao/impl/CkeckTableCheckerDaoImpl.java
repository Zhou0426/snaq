package cn.jagl.aq.dao.impl;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.CheckTableCheckerDao;
import cn.jagl.aq.domain.CheckTableChecker;

/**
 * @author mahui
 * @method 
 * @date 2016年8月9日上午8:57:00
 */
@Repository("checkTableCheckerDao")
public class CkeckTableCheckerDaoImpl extends BaseDaoHibernate5<CheckTableChecker> implements CheckTableCheckerDao {

	@Override
	public void deleteByMany(String personId, String checkTableSn) {
		String sql="DELETE FROM check_table_checker WHERE check_table_sn like '"+checkTableSn+"' and checker_id LIKE '"+personId+"'";
		getSessionFactory().getCurrentSession()
			.createSQLQuery(sql)
			.executeUpdate();
		
	}

	@Override
	public CheckTableChecker getById(String checkTableSn,String personId) {
		/*String sql="select check_table_checker.* from check_table_checker where id="+id;
		Query query=getSessionFactory().getCurrentSession()
				.createSQLQuery(sql)
				.addEntity(CheckTableChecker.class);
		System.out.println(query.uniqueResult());
		return (CheckTableChecker) query.uniqueResult(); */
		String sql="select check_table_checker.* FROM check_table_checker WHERE check_table_sn like '"+checkTableSn+"' and checker_id LIKE '"+personId+"'";
		return (CheckTableChecker) getSessionFactory().getCurrentSession().createSQLQuery(sql)
				.addEntity(CheckTableChecker.class).uniqueResult();
	}

}
