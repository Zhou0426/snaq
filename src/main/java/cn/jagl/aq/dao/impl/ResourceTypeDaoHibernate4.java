package cn.jagl.aq.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.ResourceTypeDao;
import cn.jagl.aq.domain.ResourceType;
@Repository("resourceTypeDao")
public class ResourceTypeDaoHibernate4 extends BaseDaoHibernate5<ResourceType>
	implements ResourceTypeDao 
{

}
