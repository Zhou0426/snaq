package cn.jagl.aq.service;

import java.util.List;

import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:28:37
 */
public interface UnsafeConditionCorrectConfirmService {
	//添加
	public void save(UnsafeConditionCorrectConfirm unsafeConditionCorrectConfirm);
	//查询
	public List<UnsafeConditionCorrectConfirm> query(int id);
}
