package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeActStandard;

public interface UnsafeActStandardService {

	//���ݱ�׼��Ż�ȡ��׼
	UnsafeActStandard getByUnsafeActStandardSn(String unsafeActStandardSn);
	//��ҳ��ȡ��׼
	List<UnsafeActStandard> findByPage(String hql,int page,int rows);
	//���ݲ���ģ����ѯ
	List<UnsafeActStandard> getStandardByFullTextQuery(String q,String departmentTypeSn,int page,int rows);
	//����hql����ѯ����ȫ��Ϊ��׼--����
	List<UnsafeActStandard> getByHql(String hql);
}
