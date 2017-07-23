package cn.jagl.aq.service;
import java.util.List;

import cn.jagl.aq.domain.CertificationType;
import net.sf.json.JSONObject;

public interface CertificationTypeService {	
	//hql查询总数
	public long countHql(String hql) ;
	//分页查询
	List<CertificationType> findByPage(String hql , int pageNo, int pageSize);
	//根据编号更新实体
	public CertificationType getByCertificationTypeSn(String certificationTypeSn);
	//增加未遂事件实体
	void add(CertificationType certificationType);
	//删除未遂事件实体
	void delete(CertificationType certificationType);
	//更新未遂事件实体
	void update(CertificationType certificationType);
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
