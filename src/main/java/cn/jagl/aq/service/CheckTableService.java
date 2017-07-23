package cn.jagl.aq.service;

import cn.jagl.aq.domain.CheckTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年8月2日下午3:12:59
 */
public interface CheckTableService {
	//添加
	public void save(CheckTable checkTable);
	//删除
	public void deleteById(int id);
	//修改
	public void update(CheckTable checkTable);
	//查询
	public JSONArray queryJoinCheck(String specialCheckSn,String periodicalCheckSn,String auditSn);
	//getById
	public CheckTable getById(int id);
	//查询我的检查表
	public JSONObject query(String personId,int page,int rows);
	//查看检查表详情
	public JSONObject queryDetails(int id,String periodicalCheckSn,String specialCheckSn,String auditSn,int page,int rows);
	//查询我的检查表
	public long count(String personId);
	/**
	 * 查询未确认的检查表个数
	 * @param 蒋宇森
	 * @return
	 */
	public long countExceptTrue(String personId);
}
