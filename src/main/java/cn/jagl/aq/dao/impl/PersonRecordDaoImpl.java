package cn.jagl.aq.dao.impl;


import java.time.LocalDate;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.PersonRecordDao;
import cn.jagl.aq.domain.PersonRecord;

@Repository("personRecordDao")
public class PersonRecordDaoImpl extends BaseDaoHibernate5<PersonRecord> implements PersonRecordDao {

	@Override
	public PersonRecord getByPersonIdDepartmentSn(String personId, String departmentSn) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from PersonRecord p where p.person.personId=:personId and p.department.departmentSn=:departmentSn order by p.startDateTime desc").setString("personId", personId).setString("departmentSn", departmentSn);
		return (PersonRecord)query.setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonRecord> getListByPersonId(String personId) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from PersonRecord p where p.person.personId=:personId").setString("personId", personId);
		return (List<PersonRecord>)query.list();
	}

	@Override
	public PersonRecord getByPersonId(String personId) {
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from PersonRecord p where p.person.personId=:personId order by p.startDateTime desc").setString("personId", personId);
		return (PersonRecord)query.setMaxResults(1).uniqueResult();
	}

	@Override
	public void Manydelete(String ids) {
		if(ids!=null && !"".equals(ids)){
			getSessionFactory().getCurrentSession()
			.createQuery("delete from PersonRecord p where p.id in :ids").setString("ids", ids).executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonRecord> getMonthByPersonId(String personId) {
		String hql = "select p from PersonRecord p where p.person.personId = '" + personId + "'"
				+ " AND NOT date(p.startDateTime) > '" + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()) + " 23:59:59'"
				+ " AND (NOT date(p.endDateTime) < '"+LocalDate.now().withDayOfMonth(1) + " 00:00:00' or p.endDateTime is null)";
		Query query =getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<PersonRecord>)query.list();
	}

	

}
