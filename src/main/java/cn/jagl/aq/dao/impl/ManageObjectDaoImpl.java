package cn.jagl.aq.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.ManageObjectDao;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;
@Repository("manageObjectDao")
public class ManageObjectDaoImpl extends BaseDaoHibernate5<ManageObject>
	implements ManageObjectDao
{
	@Override
	public ManageObject getByManageObjectSn(String manageObjectSn) {
		// TODO Auto-generated method stub
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from ManageObject p where manageObjectSn=:manageObjectSn").setString("manageObjectSn", manageObjectSn);
		return (ManageObject)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ManageObject> getByParentSn(String parentSn,String departmentTypeSn) {
		// TODO Auto-generated method stub
		String hql="select p from ManageObject p where p.parent.manageObjectSn=:parentSn and p.departmentType.departmentTypeSn=:departmentTypeSn";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		query=query.setParameter("parentSn", parentSn);
		query=query.setParameter("departmentTypeSn", departmentTypeSn);
		List<ManageObject> list=(List<ManageObject>)query.list();
		return list;
	}

	@Override
	public long findNum(String parentManageObjectSn) {
		// TODO Auto-generated method stub
		String hql="select COUNT(m) FROM ManageObject m WHERE m.parent.manageObjectSn=:parentManageObjectSn";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		query=query.setParameter("parentManageObjectSn", parentManageObjectSn);
		return (Long)query.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ManageObject> getByMoHuFind(String manageObjectSn) {
		String hql="from ManageObject m where m.parent is not null and (m.manageObjectSn like ? or m.manageObjectName like ?)";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		query.setString(0,"%"+manageObjectSn+"%");
		query.setString(1,"%"+manageObjectSn+"%");
		if(manageObjectSn==null||manageObjectSn.trim().length()==0){
			hql="select m from ManageObject m where m.parent is not null";
			query=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setFirstResult(0)
				.setMaxResults(20);
		}
		return (List<ManageObject>)query.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ManageObject> getManageObjectsByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<ManageObject>)getSessionFactory().getCurrentSession().createQuery(hql).list();
	}

}
