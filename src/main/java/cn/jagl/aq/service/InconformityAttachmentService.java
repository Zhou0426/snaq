package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.InconformityAttachment;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:01
 */
public interface InconformityAttachmentService {
	//添加
	void save(InconformityAttachment inconformityAttachment);
	//更新
	void update(InconformityAttachment inconformityAttachment);
	//多条删除
	void deleteByIds(String ids);
	//分页查询
	List<InconformityAttachment> query(String hql,int page,int rows);
	//记录数查询
	long count(String hql);
	//通过不符合项编号获取相关附件信息
	List<InconformityAttachment> queryJoinInconformityItem(String inconformityItemSn);
	//通过Id获取
	InconformityAttachment getById(int id);
}
