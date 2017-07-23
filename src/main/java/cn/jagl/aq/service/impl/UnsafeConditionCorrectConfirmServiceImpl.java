package cn.jagl.aq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeConditionCorrectConfirmDao;
import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;
import cn.jagl.aq.service.UnsafeConditionCorrectConfirmService;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:31:01
 */
@Service("unsafeConditionCorrectConfirmService")
public class UnsafeConditionCorrectConfirmServiceImpl implements UnsafeConditionCorrectConfirmService {
	@Resource(name="unsafeConditionCorrectConfirmDao")
	private UnsafeConditionCorrectConfirmDao unsafeConditionCorrectConfirmDao;
	@Override
	public void save(UnsafeConditionCorrectConfirm unsafeConditionCorrectConfirm) {
		unsafeConditionCorrectConfirmDao.save(unsafeConditionCorrectConfirm);
	}

	@Override
	public List<UnsafeConditionCorrectConfirm> query(int id) {
		return unsafeConditionCorrectConfirmDao.query(id);
	}

}
