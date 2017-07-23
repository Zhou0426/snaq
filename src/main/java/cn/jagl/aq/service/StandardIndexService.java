package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardType;

public interface StandardIndexService {
	void addStandardIndex(StandardIndex standardIndex);
	List<StandardIndex> getAllStandardIndexs();
	void deleteStandardIndex(int id);
	List<StandardIndex> queryJoinDocumentTemplate(String documentTemplateSn);
	StandardIndex getByindexSn(String indexSn);
	List<StandardIndex> getAll();
	//����IDɾ
	void deleteById(int id);
	//����
	void update(StandardIndex standardIndex);
	//get by Id
	StandardIndex getById(int id);
	//ɾ����Ԫ��
	public void deleteById(String indexSn);
	//��ȡ����tree
	List<StandardIndex> getPart(String hql);
	//ֻ���ǰ15����¼
	List<StandardIndex> queryByQ(String q,String standardSn,String standardType);
	//����checkTableId��ȡָ��
	List<StandardIndex> queryJoinCheckTable(int id,int page,int rows);
	//count
	public long count(String hql);
	//��˳�����list
	public List<StandardIndex> exportByStandardSn(String standardSn);
}
