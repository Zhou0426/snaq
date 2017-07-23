package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.AccidentLevel;

public interface AccidentLevelDao extends BaseDao<AccidentLevel>{

	AccidentLevel getByAccidentLevelSn(String accidentLevelSn);
}
