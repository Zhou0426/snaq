package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.LatentHazard;
import net.sf.json.JSONObject;

public interface LatentHazardDao extends BaseDao<LatentHazard> {
	/**
	 * ��ѯ�����ϱ���¼
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showData(String personId, Integer page, Integer rows);
	/**
	 * �߼�ɾ���ش������ϱ���¼
	 * @param id
	 */
	void deleteById(int id);
	/**
	 * ��ѯ��������˼�¼
	 * @param departmentSn
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showAuditData(String departmentSn, Integer page, Integer rows);
	/**
	 * ��ѯ���ż�¼
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showCancelData(Integer page, Integer rows);

}
