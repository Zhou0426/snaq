package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.ModuleStatisticsDao;
import cn.jagl.aq.service.ModuleStatisticsService;
import net.sf.json.JSONArray;

@Service("moduleStatisticsService")
public class ModuleStatisticsServiceImpl implements ModuleStatisticsService {

	@Resource(name="moduleStatisticsDao")
	private ModuleStatisticsDao moduleStatisticsDao;
	
	@Override
	public JSONArray findAll(int year, int month, String resourceSn) {
		return moduleStatisticsDao.findAll(year, month, resourceSn);
	}

}
