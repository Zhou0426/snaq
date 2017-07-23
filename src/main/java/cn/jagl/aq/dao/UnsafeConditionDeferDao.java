package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeConditionDefer;
import net.sf.json.JSONArray;

public interface UnsafeConditionDeferDao extends BaseDao<UnsafeConditionDefer> {
	public UnsafeConditionDefer getBySn(String applicationSn);

	/**
	 * 根据hql语句查询总数
	 * @param hql
	 * @return
	 */
	public long getByhql(String hql);
	/**
	 * 根据personId获取该人员审批的所有记录，按照审批时间排序
	 * @param personId
	 * @return
	 */
	public JSONArray getListByPersonId(String personId);
}
