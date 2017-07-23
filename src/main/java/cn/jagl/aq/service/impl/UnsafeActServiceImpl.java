package cn.jagl.aq.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeActDao;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.service.UnsafeActService;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:41
 */
@Service("unsafeActService")
public class UnsafeActServiceImpl implements UnsafeActService {
	@Resource(name="unsafeActDao")
	private UnsafeActDao unsafeActDao;
	//查询不符合项数目
	@Override
	public long query(String departmentSn,String departmentTypeSn,  String specialitySnIndex,
			String inconformityLevelSnIndex, String begin, String end) {
		return (long)unsafeActDao.query(departmentSn,departmentTypeSn, specialitySnIndex, inconformityLevelSnIndex, begin, end);
	}
	@Override
	public List<UnsafeAct> query(String hql, int page, int rows) {
		if( page == 0 && rows == 0 ){
			return unsafeActDao.findByHql(hql);
		}
		return unsafeActDao.findByPage(hql, page, rows);
	}
	@Override
	public List<UnsafeAct> findByPage(String hql , int page, int rows){
		return unsafeActDao.findByPage(hql, page, rows);
	}
	@Override
	public long countByHql(String hql) {
		return unsafeActDao.countByHql(hql);
	}
	@Override
	public int save(UnsafeAct unsafeAct) {
		return (Integer)unsafeActDao.save(unsafeAct);
	}
	@Override
	public void deleteByIds(String ids) {
		unsafeActDao.deleteByIds(ids);
	}
	@Override
	public UnsafeAct getById(int id) {
		return unsafeActDao.getById(id);
	}
	@Override
	public void update(UnsafeAct unsafeAct) {
		unsafeActDao.update(unsafeAct);
	}
	@Override
	public UnsafeAct getBySn(String unsafeActSn) {
		return unsafeActDao.getBySn(unsafeActSn);
	}
	@Override
	public Map<String, Object> showData(String checkDeptSn, String departmentSn, String str, String specialitySn,
			String unsafeActStandardSn, String checkerFromSn, String checkTypeSn, String unsafeActLevelSn,
			String timeData, Timestamp beginTime, Timestamp endTime, String checkers, int page, int rows) 
	{
		return unsafeActDao.showData(checkDeptSn, departmentSn, str, specialitySn,
				unsafeActStandardSn, checkerFromSn, checkTypeSn, unsafeActLevelSn,
				timeData, beginTime, endTime, checkers, page, rows);
	}
	@Override
	public Map<String, Object> queryMyUnsafeAct(String personId, Integer page, Integer rows) {
		return unsafeActDao.queryMyUnsafeAct(personId, page, rows);
	}

}
