package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.Resource;

public interface ResourceDao extends BaseDao<Resource> {

	Resource getByResourceSn(String resourceSn);
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
	List<Resource> getResourceByParentSn(String resourceSn);
	/**
	 * ��ȡ���еĲ˵���Դ��
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016-10-1
	 */
	List<Resource> getAllMenuResources();
}
