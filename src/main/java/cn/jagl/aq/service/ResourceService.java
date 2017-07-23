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
	 * ��ȡ��Դ����Ϊmenu������
	 * @param resourceSn
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016-7-25
	 */
	List<Resource> getMenuTypeChildren(String resourceSn);
	/**
	 * ���ݸ���Ȩ�޻�ȡ����Ȩ��
	 * @param resourceSn
	 * @return
	 * 
	 * @author ë��¶
	 * @date 2016-9-1
	 */
	List<Resource> getResourceByParentSn(String id);

	List<Resource> getByHql(String hql);
	/**
	 * ��ȡ���еĲ˵���Դ��
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016-10-1
	 */
	public List<Resource> getAllMenuResources();
	 
}
