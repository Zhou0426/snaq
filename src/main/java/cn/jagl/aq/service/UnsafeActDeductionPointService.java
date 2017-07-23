package cn.jagl.aq.service;

import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import net.sf.json.JSONObject;

/**
 *
 * @author 马辉
 * @since JDK1.8
 * @history 2017年5月5日下午9:37:27 马辉 新建
 */
public interface UnsafeActDeductionPointService {

    JSONObject query(Byte type,String departmentTypeSn,String departmentSn,int page,int rows);
	
	UnsafeActDeductionPoint getByMany(Byte type,String departmentTypeSn,String departmentSn,int year,int level);

	UnsafeActDeductionPoint getById(int id);
	
	void save(UnsafeActDeductionPoint unsafeActDeductionPoint);
	
	void update(UnsafeActDeductionPoint unsafeActDeductionPoint);
	
	void delete(int id);
}
