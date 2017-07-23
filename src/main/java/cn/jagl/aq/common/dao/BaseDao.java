package cn.jagl.aq.common.dao;

import java.util.List;
import java.io.Serializable;


public interface BaseDao<T>
{
	// ����ID����ʵ��
	T get(Class<T> entityClazz , Serializable id);
	// ����ʵ��
	Serializable save(T entity);
	// ����ʵ��
	void update(T entity);
	// ɾ��ʵ��
	void delete(T entity);
	// ����IDɾ��ʵ��
	void delete(Class<T> entityClazz , Serializable id);
	// ��ȡ����ʵ��
	List<T> findAll(Class<T> entityClazz);
	//���ݷ�ҳ��ȡ����
	List<T> findByPage(String hql , int pageNo, int pageSize);
	// ��ȡʵ������
	long findCount(Class<T> entityClazz);
	//���ݴ��η�ҳ��ȡ����
	List<T> findByPage(String hql , int pageNo, int pageSize,Object...params);
}
