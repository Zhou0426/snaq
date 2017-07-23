package cn.jagl.aq.dao;

import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import net.sf.json.JSONObject;

/**
 *
 * @author 马辉
 * @since JDK1.8
 * @history 2017年5月5日下午9:34:43 马辉 新建
 */
public interface UnsafeActDeductionPointDao extends BaseDao<UnsafeActDeductionPoint> {
	
	JSONObject query(Byte type,String departmentTypeSn,String departmentSn,int page,int rows);
	
	UnsafeActDeductionPoint getByMany(Byte type,String departmentTypeSn,String departmentSn,int year,int level);
}
