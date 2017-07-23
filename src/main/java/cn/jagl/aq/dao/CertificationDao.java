package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Certification;

public interface CertificationDao extends BaseDao<Certification>{


	//hql查询总数
	public long countHql(String hql) ;
	//根据事故编号更新实体
	public Certification getByCertificationSn(String certificationSn);	
	//根据id获取实体
	public Certification getById(int id);
	/**
	 * 根据证件编号和证件类型编号查询实体
	 * @param certificationSn
	 * @param certificationTypeSn
	 * @return
	 */
	public Certification getByCertificationSnAndTypeSn(String certificationSn, String certificationTypeSn);
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteByIds(String ids);
}
