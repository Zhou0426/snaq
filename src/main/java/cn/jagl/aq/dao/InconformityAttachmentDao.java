package cn.jagl.aq.dao;
/*
 * ���
 * 2016/7/8
 */

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.InconformityAttachment;
public interface InconformityAttachmentDao extends BaseDao<InconformityAttachment>{
	//��¼����ѯ
	public long count(String hql);
	//����ɾ��
	public void deleteByIds(String ids);
	//ͨ�����������Ż�ȡ��ظ�����Ϣ
	public List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn);
}
