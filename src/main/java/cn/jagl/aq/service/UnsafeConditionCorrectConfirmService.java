package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;

/**
 * @author mahui
 * @method 
 * @date 2016��7��23������4:28:37
 */
public interface UnsafeConditionCorrectConfirmService {
	//���
	public void save(UnsafeConditionCorrectConfirm unsafeConditionCorrectConfirm);
	//��ѯ
	public List<UnsafeConditionCorrectConfirm> query(int id);
}
