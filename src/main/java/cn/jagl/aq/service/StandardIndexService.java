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
	//根据ID删
	void deleteById(int id);
	//更新
	void update(StandardIndex standardIndex);
	//get by Id
	StandardIndex getById(int id);
	//删除子元素
	public void deleteById(String indexSn);
	//获取部分tree
	List<StandardIndex> getPart(String hql);
	//只输出前15条记录
	List<StandardIndex> queryByQ(String q,String standardSn,String standardType);
	//根据checkTableId获取指标
	List<StandardIndex> queryJoinCheckTable(int id,int page,int rows);
	//count
	public long count(String hql);
	//按顺序输出list
	public List<StandardIndex> exportByStandardSn(String standardSn);
}
