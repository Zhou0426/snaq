package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.AccidentLevel;
import cn.jagl.aq.domain.AccidentType;

public interface AccidentDao extends BaseDao<Accident>{

	//hql查询总数
	public long countHql(String hql) ;
	//根据事故编号更新实体
	public Accident getByAccidentSn(String accidentSn);	
}
