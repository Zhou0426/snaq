package cn.jagl.aq.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.CheckTableDao;
import cn.jagl.aq.domain.CheckTable;
import cn.jagl.aq.service.CheckTableService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016年8月2日下午3:16:40
 */
@Service("checkTableService")
public class CheckTableServiceImpl implements CheckTableService {
	
	@Resource(name="checkTableDao")
	private CheckTableDao checkTableDao;
	@Override
	public void save(CheckTable checkTable) {
		checkTableDao.save(checkTable);
	}

	@Override
	public void deleteById(int id) {
		checkTableDao.delete(CheckTable.class, id);
	}

	@Override
	public void update(CheckTable checkTable) {
		checkTableDao.update(checkTable);
	}

	@Override
	public JSONArray queryJoinCheck(String specialCheckSn, String periodicalCheckSn,String auditSn) {	
		return checkTableDao.queryJoinCheck(specialCheckSn, periodicalCheckSn, auditSn);
	}

	@Override
	public CheckTable getById(int id) {
		return checkTableDao.get(CheckTable.class, id);
	}

	@Override
	public JSONObject query(String personId, int page, int rows) {
		return checkTableDao.query(personId, page, rows);
	}

	@Override
	public long count(String personId) {
		// TODO Auto-generated method stub
		return checkTableDao.count(personId);
	}

	@Override
	public long countExceptTrue(String personId) {
		// TODO Auto-generated method stub
		return checkTableDao.countExceptTrue(personId);
	}

	//查看检查表详情
	@Override
	public JSONObject queryDetails(int id, String periodicalCheckSn, String specialCheckSn, String auditSn, int page,
			int rows) {
		return checkTableDao.queryDetails(id, periodicalCheckSn, specialCheckSn, auditSn, page, rows);
	}

}
