package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.AccidentType;

public interface AccidentTypeService {

	int addAccidentType(AccidentType accidentType);
	
	void deleteAccidentType(int id);
	
	List<AccidentType> getAllAccidentType();
	
	AccidentType getByAccidentTypeSn(String accidentTypeSn);
	
}
