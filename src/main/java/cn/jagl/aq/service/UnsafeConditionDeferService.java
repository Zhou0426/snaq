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
	 * ����hql����ѯ����
	 * @param hql
	 * @return
	 */
	public long getByhql(String hql);
	/**
	 * ����personId��ȡ����Ա���������м�¼����������ʱ������
	 * @param personId
	 * @return
	 */
	public JSONArray getListByPersonId(String personId);
}
