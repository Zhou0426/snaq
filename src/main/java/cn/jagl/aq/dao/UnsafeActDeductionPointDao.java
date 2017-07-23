package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import net.sf.json.JSONObject;

/**
 *
 * @author ���
 * @since JDK1.8
 * @history 2017��5��5������9:34:43 ��� �½�
 */
public interface UnsafeActDeductionPointDao extends BaseDao<UnsafeActDeductionPoint> {
	
	JSONObject query(Byte type,String departmentTypeSn,String departmentSn,int page,int rows);
	
	UnsafeActDeductionPoint getByMany(Byte type,String departmentTypeSn,String departmentSn,int year,int level);
}
