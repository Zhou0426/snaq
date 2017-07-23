package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;
import cn.jagl.aq.dao.NearMissTypeDao;
import cn.jagl.aq.domain.NearMissType;
import cn.jagl.aq.service.NearMissTypeService;
import org.springframework.stereotype.Service;
@Service("nearMissTypeService")
public class NearMissTypeServiceImpl implements NearMissTypeService{
	@Resource(name="nearMissTypeDao")
	private NearMissTypeDao nearMissTypeDao;
	@Override
	public NearMissType getByNearMissTypeSn(String nearMissTypeSn) {
		return nearMissTypeDao.getByNearMissTypeSn(nearMissTypeSn);
	}
	@Override
	public List<NearMissType> getAllNearMissType() {
		return nearMissTypeDao.getAllNearMissType();
	}

}
