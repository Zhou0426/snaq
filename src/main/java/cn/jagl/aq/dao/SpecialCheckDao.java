package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SpecialCheck;

/**
 * @author mahui
 *
 * @date 2016��7��8������5:15:05
 */
public interface SpecialCheckDao extends BaseDao<SpecialCheck> {
	//��Ų�ѯ
	public SpecialCheck getBySpecialCheckSn(String specialCheckSn);
	//��¼��
	public long count(String hql);
	//����ɾ��
	public void deleteByIds(String ids);

}
