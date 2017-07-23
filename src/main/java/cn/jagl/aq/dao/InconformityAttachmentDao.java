package cn.jagl.aq.dao;
/*
 * 马辉
 * 2016/7/8
 */

import java.util.List;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.InconformityAttachment;
public interface InconformityAttachmentDao extends BaseDao<InconformityAttachment>{
	//记录数查询
	public long count(String hql);
	//多条删除
	public void deleteByIds(String ids);
	//通过不符合项编号获取相关附件信息
	public List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn);
}
