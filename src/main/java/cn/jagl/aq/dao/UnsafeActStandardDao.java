package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeActStandard;

public interface UnsafeActStandardDao extends BaseDao<UnsafeActStandard> {

	//���ݱ�׼��Ż�ȡ��׼
	public UnsafeActStandard getByUnsafeActStandard(String unsafeActStandardSn);
	//���ݲ���ģ����ѯ
	public List<UnsafeActStandard> getStandardByFullTextQuery(String q,String departmentTypeSn,int page,int rows);
	//����hql����ѯ����ȫ��Ϊ��׼
	public List<UnsafeActStandard> getByHql(String hql);
}
