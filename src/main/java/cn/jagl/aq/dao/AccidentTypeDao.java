package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.AccidentType;

public interface AccidentTypeDao extends BaseDao<AccidentType> {

	public AccidentType getByAccidentTypeSn(String accidentTypeSn);
}
