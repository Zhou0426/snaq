package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeConditionDefer;
import net.sf.json.JSONArray;

public interface UnsafeConditionDeferService {
	public UnsafeConditionDefer getBySn(String applicationSn);
	public List<UnsafeConditionDefer> queryByPage(String hql,int page,int rows);
	public void save(UnsafeConditionDefer unsafeConditionDefer);
	public void update(UnsafeConditionDefer unsafeConditionDefer);
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
