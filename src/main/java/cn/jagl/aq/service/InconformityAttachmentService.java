package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.InconformityAttachment;

/**
 * @author mahui
 *
 * @date 2016��7��8������3:05:01
 */
public interface InconformityAttachmentService {
	//���
	void save(InconformityAttachment inconformityAttachment);
	//����
	void update(InconformityAttachment inconformityAttachment);
	//����ɾ��
	void deleteByIds(String ids);
	//��ҳ��ѯ
	List<InconformityAttachment> query(String hql,int page,int rows);
	//��¼����ѯ
	long count(String hql);
	//ͨ�����������Ż�ȡ��ظ�����Ϣ
	List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn);
	//ͨ��Id��ȡ
	InconformityAttachment getById(int id);
}
