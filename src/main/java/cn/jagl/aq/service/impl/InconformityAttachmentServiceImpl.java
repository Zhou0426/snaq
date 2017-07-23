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
 * @date 2016��7��8������3:05:33
 */
@Service("inconformityAttachmentService")
public class InconformityAttachmentServiceImpl implements InconformityAttachmentService {
	@Resource(name="inconformityAttachmentDao")
	private InconformityAttachmentDao inconformityAttachmentDao;
	//���
	@Override
	public void save(InconformityAttachment inconformityAttachment) {
		inconformityAttachmentDao.save(inconformityAttachment);		
	}

	//����
	@Override
	public void update(InconformityAttachment inconformityAttachment) {
		inconformityAttachmentDao.update(inconformityAttachment);		
	}
	
	//ɾ��
	@Override
	public void deleteByIds(String ids) {
		inconformityAttachmentDao.deleteByIds(ids);		
	}
	//��ҳ��ѯ
	@Override
	public List<InconformityAttachment> query(String hql, int page, int rows) {
		List<InconformityAttachment> list=inconformityAttachmentDao.findByPage(hql, page, rows);
		return list;
	}

	@Override
	public long count(String hql) {
		return (Long)inconformityAttachmentDao.count(hql);
	}
	
	//ͨ�����������Ż�ȡ��ظ�����Ϣ
	@Override
	public List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn) {
		return inconformityAttachmentDao.queryJoinInconformityItem(inconformityItemSn);
	}
	
	//ͨ��Id��ȡ
	@Override
	public InconformityAttachment getById(int id) {
		// TODO Auto-generated method stub
		return (InconformityAttachment)inconformityAttachmentDao.get(InconformityAttachment.class, id);
	}
	
}
