package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.StandardIndex;

public interface StandardIndexDao extends BaseDao<StandardIndex>{
	public List<StandardIndex> queryJoinDocumentTemplate(String documentTemplateSn);

	public StandardIndex getByindexSn(String indexSn);
	
	public List<StandardIndex> getAll();
	//ͨ��ID��ȡ��BaseDao�ķ����޷�ִ�У�
	public StandardIndex getById(int id);
	//��������ѯ
	public List<StandardIndex> getPart(String hql);
	//ɾ����Ԫ�ؼ���Ԫ��
	public void deleteById(String indexSn);
	//ģ����ѯָ��
	public List<StandardIndex> getByQ(String q,String standardSn,String standardType);
	//����checkTableId��ȡָ��
	public List<StandardIndex> queryJoinCheckTable(int id,int page,int rows);

	//count
	public long count(String hql);
	//��˳�����list
	public List<StandardIndex> exportByStandardSn(String standardSn);
}
