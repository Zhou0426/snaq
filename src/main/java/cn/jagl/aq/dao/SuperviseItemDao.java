package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SuperviseItem;

public interface SuperviseItemDao extends BaseDao<SuperviseItem>{
	
	SuperviseItem getSuperviseItemBySn(String superviseItemSn);


	List<SuperviseItem> getSuperviseItemsByType(String departmentTypeSn);

	List<SuperviseItem> getSuperviseParentItemsByType(String departmentTypeSn);
}
