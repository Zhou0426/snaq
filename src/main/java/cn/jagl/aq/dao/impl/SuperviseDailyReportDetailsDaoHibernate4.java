package cn.jagl.aq.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.SuperviseDailyReportDetailsDao;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.aq.domain.SuperviseDailyReportDetails;
@Repository("superviseDailyReportDetailsDao")
public class SuperviseDailyReportDetailsDaoHibernate4 extends BaseDaoHibernate5<SuperviseDailyReportDetails>
	implements SuperviseDailyReportDetailsDao {


	@SuppressWarnings("unchecked")
	@Override
	public List<SuperviseDailyReportDetails> getSuperviseDailyReportDetailsByDateandDept(SuperviseDailyReport superviseDailyReport, String departmentSn) {
		String hql="select p from SuperviseDailyReportDetails p where p.superviseDailyReport.reportDate=:reportDate and p.department.departmentSn=:departmentSn";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql)
				.setDate("reportDate", superviseDailyReport.getCreatedTime())
				.setString("departmentSn", departmentSn);
		return (List<SuperviseDailyReportDetails>)query.list();
	}

	@Override
	public SuperviseDailyReportDetails getByMany(int id, String departmentSn,
			String superviseItemSn) {
		String hql="select p from SuperviseDailyReportDetails p where p.superviseDailyReport.id=:id and p.department.departmentSn=:departmentSn and p.superviseItem.superviseItemSn=:superviseItemSn";
		return (SuperviseDailyReportDetails) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setInteger("id", id)
				.setString("departmentSn", departmentSn)
				.setString("superviseItemSn", superviseItemSn)
				.uniqueResult();
	}

	


}
