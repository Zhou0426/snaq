package cn.jagl.aq.dao;

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.StandardIndex;

public interface StandardIndexDao extends BaseDao<StandardIndex>{
	public List<StandardIndex> queryJoinDocumentTemplate(String documentTemplateSn);

	public StandardIndex getByindexSn(String indexSn);
	
	public List<StandardIndex> getAll();
	//通过ID获取（BaseDao的方法无法执行）
	public StandardIndex getById(int id);
	//按条件查询
	public List<StandardIndex> getPart(String hql);
	//删除父元素及子元素
	public void deleteById(String indexSn);
	//模糊查询指标
	public List<StandardIndex> getByQ(String q,String standardSn,String standardType);
	//根据checkTableId获取指标
	public List<StandardIndex> queryJoinCheckTable(int id,int page,int rows);

	//count
	public long count(String hql);
	//按顺序输出list
	public List<StandardIndex> exportByStandardSn(String standardSn);
}
