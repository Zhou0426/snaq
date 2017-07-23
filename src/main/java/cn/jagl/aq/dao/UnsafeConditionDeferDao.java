package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeConditionDefer;
import net.sf.json.JSONArray;

public interface UnsafeConditionDeferDao extends BaseDao<UnsafeConditionDefer> {
	public UnsafeConditionDefer getBySn(String applicationSn);

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
