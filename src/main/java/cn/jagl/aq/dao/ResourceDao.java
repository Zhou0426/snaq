package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Resource;

public interface ResourceDao extends BaseDao<Resource> {

	Resource getByResourceSn(String resourceSn);
	/**
	 * 获取资源类型为menu的子项
	 * @param resourceSn
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016-7-25
	 */
	List<Resource> getMenuTypeChildren(String resourceSn);
	/**
	 * 根据父级权限获取子类权限
	 * @param resourceSn
	 * @return
	 * 
	 * @author 毛凯露
	 * @date 2016-9-1
	 */
	List<Resource> getResourceByParentSn(String resourceSn);
	/**
	 * 获取所有的菜单资源项
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016-10-1
	 */
	List<Resource> getAllMenuResources();
}
