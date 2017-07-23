package cn.jagl.aq.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.Resource;

public interface ResourceService {

	int addResource(Resource resource);
	
	List<Resource> getAllResources();
	
	void deleteResource(int id);
	
	Resource getByResourceSn(String resourceSn);
	
	Resource getById(int id);
	
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
	List<Resource> getResourceByParentSn(String id);

	List<Resource> getByHql(String hql);
	/**
	 * 获取所有的菜单资源项
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016-10-1
	 */
	public List<Resource> getAllMenuResources();
	 
}
