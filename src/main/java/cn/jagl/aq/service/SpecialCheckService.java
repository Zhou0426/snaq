package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.SpecialCheck;

/**
 * @author mahui
 *
 * @date 2016��7��8������5:16:10
 */
public interface SpecialCheckService {
	//���
	void save(SpecialCheck specialCheck);
	//����
	void update(SpecialCheck specialCheck);
	//����ɾ��
	void deleteByIds(String ids);
	//��Ż�ȡ
	SpecialCheck getBySpecialCheckSn(String specialCheckSn);
	//��ȡ��¼��
	long count(String hql);
	//��ҳ��ѯ
	List<SpecialCheck> query(String hql,int page,int rows);
	//hetById
	SpecialCheck getById(int id);
}
