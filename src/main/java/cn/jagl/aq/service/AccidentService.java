package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.Accident;
import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.NearMiss;

public interface AccidentService {

	//hql查询总数
	public long countHql(String hql) ;
	//分页查询
	List<Accident> findByPage(String hql , int pageNo, int pageSize);
	//根据事故编号更新实体
	public Accident getByAccidentSn(String accidentSn);	
	//增加未遂事件实体
	void add(Accident accident);
	//删除未遂事件实体
	void delete(Accident accident);
	//更新未遂事件实体
	void update(Accident accident);
}
