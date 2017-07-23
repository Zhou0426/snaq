package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.CertificationType;
import net.sf.json.JSONObject;

public interface CertificationTypeDao extends BaseDao<CertificationType>{

	//hql查询总数
	public long countHql(String hql) ;
	//根据编号更新实体
	public CertificationType getByCertificationTypeSn(String certificationTypeSn);
	//根据id获取实体
	public CertificationType getById(int id);
	public CertificationType getByCertificationTypeName(String stringValue);
	/**
	 * 根据部门编号查询该部门证件类型持有人的数量
	 * @param departmentSn
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject queryStatistics(String departmentSn, int page, int rows);
}
