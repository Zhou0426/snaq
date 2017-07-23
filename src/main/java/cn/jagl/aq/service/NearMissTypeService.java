package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.NearMissType;

public interface NearMissTypeService {
	//通过未遂事件类型编号获取未遂事件类型
	NearMissType getByNearMissTypeSn(String nearMissTypeSn);
	//获取所有未遂事件类型编号
	List<NearMissType> getAllNearMissType();
}
