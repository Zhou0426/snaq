package cn.jagl.aq.service;

import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import net.sf.json.JSONObject;

/**
 *
 * @author ���
 * @since JDK1.8
 * @history 2017��5��5������9:37:27 ��� �½�
 */
public interface UnsafeActDeductionPointService {

    JSONObject query(Byte type,String departmentTypeSn,String departmentSn,int page,int rows);
	
	UnsafeActDeductionPoint getByMany(Byte type,String departmentTypeSn,String departmentSn,int year,int level);

	UnsafeActDeductionPoint getById(int id);
	
	void save(UnsafeActDeductionPoint unsafeActDeductionPoint);
	
	void update(UnsafeActDeductionPoint unsafeActDeductionPoint);
	
	void delete(int id);
}
