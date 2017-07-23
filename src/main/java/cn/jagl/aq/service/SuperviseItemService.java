package cn.jagl.aq.service;

import java.util.List;
import cn.jagl.aq.domain.SuperviseItem;
public interface SuperviseItemService {
	
	List<SuperviseItem> getSuperviseItemsByType(String departmentTypeSn);
	
	SuperviseItem getSuperviseItemBySn(String superviseItemSn);
}
