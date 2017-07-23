package cn.jagl.aq.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.NoticeAttachmentDao;
import cn.jagl.aq.domain.NoticeAttachment;

@Repository("noticeAttachmentDao")
public class NoticeAttachmentDaoImpl extends BaseDaoHibernate5<NoticeAttachment> implements NoticeAttachmentDao {


}
