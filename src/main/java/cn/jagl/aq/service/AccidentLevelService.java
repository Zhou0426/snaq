package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.AccidentLevel;

public interface AccidentLevelService {
	List<AccidentLevel> getAllAccidentLevel();
	AccidentLevel getByAccidentLevelSn(String accidentLevelSn);
}
