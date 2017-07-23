package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeActDeductionPointDao;
import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import cn.jagl.aq.service.UnsafeActDeductionPointService;
import net.sf.json.JSONObject;

/**
 *
 * @author 马辉
 * @since JDK1.8
 * @history 2017年5月5日下午9:37:50 马辉 新建
 */
@Service("unsafeActDeductionPointService")
public class UnsafeActDeductionPointServiceImpl implements UnsafeActDeductionPointService {

	@Resource(name="unsafeActDeductionPointDao")
	private UnsafeActDeductionPointDao unsafeActDeductionPointDao;
	
	@Override
	public JSONObject query(Byte type, String departmentTypeSn, String departmentSn, int page, int rows) {
		// TODO Auto-generated method stub
		return unsafeActDeductionPointDao.query(type, departmentTypeSn, departmentSn, page, rows);
	}

	@Override
	public UnsafeActDeductionPoint getByMany(Byte type, String departmentTypeSn, String departmentSn, int year,
			int level) {
		// TODO Auto-generated method stub
		return unsafeActDeductionPointDao.getByMany(type, departmentTypeSn, departmentSn, year, level);
	}

	@Override
	public UnsafeActDeductionPoint getById(int id) {
		// TODO Auto-generated method stub
		return unsafeActDeductionPointDao.get(UnsafeActDeductionPoint.class, id);
	}

	@Override
	public void save(UnsafeActDeductionPoint unsafeActDeductionPoint) {
		// TODO Auto-generated method stub
		unsafeActDeductionPointDao.save(unsafeActDeductionPoint);
	}

	@Override
	public void update(UnsafeActDeductionPoint unsafeActDeductionPoint) {
		// TODO Auto-generated method stub
		unsafeActDeductionPointDao.update(unsafeActDeductionPoint);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		unsafeActDeductionPointDao.delete(UnsafeActDeductionPoint.class, id);
	}

}
