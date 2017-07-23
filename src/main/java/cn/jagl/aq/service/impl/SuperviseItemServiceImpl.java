package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.SuperviseItemDao;
import cn.jagl.aq.domain.SuperviseItem;
import cn.jagl.aq.service.SuperviseItemService;
@Service("superviseItemService")
public class SuperviseItemServiceImpl implements SuperviseItemService{

	@Resource(name="superviseItemDao")
	private SuperviseItemDao superviseItemDao;
	

	@Override
	public SuperviseItem getSuperviseItemBySn(String superviseItemSn) {
		return superviseItemDao.getSuperviseItemBySn(superviseItemSn);
	}

	@Override
	public List<SuperviseItem> getSuperviseItemsByType(String departmentTypeSn) {
		return superviseItemDao.getSuperviseItemsByType(departmentTypeSn);
	}

}
