package cn.jagl.aq.service;

import cn.jagl.aq.domain.LatentHazard;
import net.sf.json.JSONObject;

public interface LatentHazardService {
	
	/**
	 * ��ѯ�����ϱ���¼
	 * @param personId
	 * @param page
	 * @param rows
	 * @return
	 */
	JSONObject showData(String personId, Integer page, Integer rows);
	/**
	 * ��������
	 * @param latentHazard
	 */
	void add(LatentHazard latentHazard);
	/**
	 * ����id��ѯ����
	 * @param id
	 * @return
	 */
	LatentHazard queryById(int id);
	/**
	 * ��������
	 * @param latentHazard
	 */
	void update(LatentHazard latentHazard);
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
