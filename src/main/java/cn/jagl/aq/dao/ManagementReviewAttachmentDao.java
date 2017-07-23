package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.ManagementReviewAttachment;

public interface  ManagementReviewAttachmentDao  extends BaseDao<ManagementReviewAttachment>{

	ManagementReviewAttachment getByPath(String path);
	ManagementReviewAttachment getById(int attid);

}
