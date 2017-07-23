package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.InconformityAttachmentDao;
import cn.jagl.aq.domain.InconformityAttachment;
import cn.jagl.aq.service.InconformityAttachmentService;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:33
 */
@Service("inconformityAttachmentService")
public class InconformityAttachmentServiceImpl implements InconformityAttachmentService {
	@Resource(name="inconformityAttachmentDao")
	private InconformityAttachmentDao inconformityAttachmentDao;
	//添加
	@Override
	public void save(InconformityAttachment inconformityAttachment) {
		inconformityAttachmentDao.save(inconformityAttachment);		
	}

	//更新
	@Override
	public void update(InconformityAttachment inconformityAttachment) {
		inconformityAttachmentDao.update(inconformityAttachment);		
	}
	
	//删除
	@Override
	public void deleteByIds(String ids) {
		inconformityAttachmentDao.deleteByIds(ids);		
	}
	//分页查询
	@Override
	public List<InconformityAttachment> query(String hql, int page, int rows) {
		List<InconformityAttachment> list=inconformityAttachmentDao.findByPage(hql, page, rows);
		return list;
	}

	@Override
	public long count(String hql) {
		return (Long)inconformityAttachmentDao.count(hql);
	}
	
	//通过不符合项编号获取相关附件信息
	@Override
	public List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn) {
		return inconformityAttachmentDao.queryJoinInconformityItem(inconformityItemSn);
	}
	
	//通过Id获取
	@Override
	public InconformityAttachment getById(int id) {
		// TODO Auto-generated method stub
		return (InconformityAttachment)inconformityAttachmentDao.get(InconformityAttachment.class, id);
	}
	
}
