package cn.jagl.aq.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnsafeConditionDao;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.service.UnsafeConditionService;

/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:05:41
 */
@Service("unsafeConditionService")
public class UnsafeConditionServiceImpl implements UnsafeConditionService {
	@Resource(name="unsafeConditionDao")
	private UnsafeConditionDao unsafeConditionDao;
	//添加
	@Override
	public void save(UnsafeCondition unsafeCondition) {
		unsafeConditionDao.save(unsafeCondition);		
	}
	//更新
	@Override
	public void update(UnsafeCondition unsafeCondition) {
		unsafeConditionDao.update(unsafeCondition);		
	}
	//多条删除
	@Override
	public void deleteByIds(String ids) {
		unsafeConditionDao.deleteByIds(ids);
	}
	//分页查询
	@Override
	public List<UnsafeCondition> query(String hql, int page, int rows) {
		List<UnsafeCondition> unsafeConditionList = new ArrayList<UnsafeCondition>();
		if( page == 0 && rows == 0 ){
			unsafeConditionList = unsafeConditionDao.findByHql(hql);
		}else{
			unsafeConditionList = unsafeConditionDao.findByPage(hql, page, rows);
		}
		//List<InconformityItem> inconformityItemList=inconformityItemDao.query(hql, page, rows);
		return unsafeConditionList;
	}
	@Override
	public List<InconformityItem> queryInconformityItem(String hql, int page, int rows) {
		List<InconformityItem> unsafeConditionList = new ArrayList<InconformityItem>();
		if( page == 0 && rows == 0 ){
			unsafeConditionList = unsafeConditionDao.findInconformityItemByHql(hql);
		}else{
			unsafeConditionList = unsafeConditionDao.findInconformityItemByPage(hql, page, rows);
		}
		//List<InconformityItem> inconformityItemList=inconformityItemDao.query(hql, page, rows);
		return unsafeConditionList;
	}
	//记录数
	@Override
	public long count(String hql) {
		return (Long)unsafeConditionDao.count(hql);
	}
	//通过编号获取
	@Override
	public UnsafeCondition getByInconformityItemSn(String inconformityItemSn) {
		return (UnsafeCondition)unsafeConditionDao.getByInconformityItemSn(inconformityItemSn);
	}
	//getBYID
	@Override
	public UnsafeCondition getById(int id) {
		return (UnsafeCondition)unsafeConditionDao.getById(id);
	}
	//hql查询总数
	@Override
	public long countHql(String hql) {
		// TODO Auto-generated method stub
		return (Long)unsafeConditionDao.countHql(hql);
	}
	//查询不符合项数目
	@Override
	public long query(String departmentSn,String departmentTypeSn,  String specialitySnIndex,
			String inconformityLevelSnIndex, String begin, String end) {
		return (long)unsafeConditionDao.query(departmentSn,departmentTypeSn, specialitySnIndex, inconformityLevelSnIndex, begin, end);
	}
	@Override
	public List<?> getBySql(String sql) {
		// TODO Auto-generated method stub
		return unsafeConditionDao.getBySql(sql);
	}
	@Override
	@Scheduled(cron="0 0/5 * * * ?")
	public void computeNowRiskLevel() {
		unsafeConditionDao.computeNowRiskLevel();
	}
	@Override
	public Map<String, Object> showData(String checkDeptSn,String departmentSn,String str,String indexSn,String standardSn,String inconformityLevel,String qSpecialitySn,
			String riskLevel,String checkType,String checkerFrom,String inconformityItemNature,String correctPrincipal,
			String hasCorrectConfirmed,String hasReviewed,String hasCorrectFinished,String timeData,String checkers,
			String pag,int page,int rows,Timestamp beginTime,Timestamp endTime, boolean checked) {
		// TODO Auto-generated method stub
		return unsafeConditionDao.showData(checkDeptSn,departmentSn,str,indexSn,standardSn,inconformityLevel,qSpecialitySn,
				riskLevel,checkType,checkerFrom,inconformityItemNature,correctPrincipal,
				hasCorrectConfirmed,hasReviewed,hasCorrectFinished,timeData,checkers,
				pag,page,rows,beginTime,endTime, checked);
	}
	@Override
	public Map<String, Object> myUnsafeCondition(String personId, int page, int rows) {
		return unsafeConditionDao.myUnsafeCondition(personId, page, rows);
	}

}
