package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.StandardIndexDao;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.service.StandardIndexService;
@Service("standardIndexService")
public class StandardIndexServiceImpl implements StandardIndexService {
	@Resource(name="standardIndexDao")
	private StandardIndexDao standardindexDao;

	@Override
	public void addStandardIndex(StandardIndex standardIndex) {
		// TODO Auto-generated method stub
		standardindexDao.save(standardIndex);
	}

	@Override
	public List<StandardIndex> getAllStandardIndexs() {
		// TODO Auto-generated method stub
		return standardindexDao.findAll(StandardIndex.class);
	}

	@Override
	public void deleteStandardIndex(int id) {
		// TODO Auto-generated method stub
		standardindexDao.delete(StandardIndex.class,id);
	}

	@Override
	public List<StandardIndex> queryJoinDocumentTemplate(String documentTemplateSn) {
		// TODO Auto-generated method stub
		return standardindexDao.queryJoinDocumentTemplate(documentTemplateSn);
	}

	@Override
	public StandardIndex getByindexSn(String indexSn) {
		// TODO Auto-generated method stub
		return standardindexDao.getByindexSn(indexSn);
	}

	@Override
	public List<StandardIndex> getAll() {
		return standardindexDao.getAll();
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		standardindexDao.delete(StandardIndex.class, id);
	}
	//更新
	@Override
	public void update(StandardIndex standardIndex) {
		// TODO Auto-generated method stub
		standardindexDao.update(standardIndex);
		
	}
	//get ById
	@Override
	public StandardIndex getById(int id) {
		// TODO Auto-generated method stub
		//return (StandardIndex)standardindexDao.get(StandardIndex.class, id);
		return (StandardIndex)standardindexDao.getById(id);
	}
	//获取部分tree
	@Override
	public List<StandardIndex> getPart(String hql) {
		// TODO Auto-generated method stub
		return standardindexDao.getPart(hql);
	}
	//一次输出15条记录，供模板添加指标时使用
	@Override
	public List<StandardIndex> queryByQ(String q,String standardSn,String standardType) {
		return standardindexDao.getByQ(q,standardSn,standardType);
	}

	//根据checkTableId获取指标
	@Override
	public List<StandardIndex> queryJoinCheckTable(int id,int page,int rows) {
		return standardindexDao.queryJoinCheckTable(id,page,rows);
	}

	@Override
	public void deleteById(String indexSn) {
		standardindexDao.deleteById(indexSn);
	}

	@Override
	public long count(String hql) {
		// TODO Auto-generated method stub
		return standardindexDao.count(hql);
	}

	//按顺序输出list
	@Override
	public List<StandardIndex> exportByStandardSn(String standardSn) {
		return standardindexDao.exportByStandardSn(standardSn);
	}




}
