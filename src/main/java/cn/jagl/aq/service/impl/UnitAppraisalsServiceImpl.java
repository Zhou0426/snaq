package cn.jagl.aq.service.impl;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.UnitAppraisalsDao;
import cn.jagl.aq.domain.UnitAppraisals;
import cn.jagl.aq.service.UnitAppraisalsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author mahui
 * @method 
 * @date 2016��8��17������6:14:48
 */
@Service("unitAppraisalsService")
public class UnitAppraisalsServiceImpl implements UnitAppraisalsService {
	@Resource(name="unitAppraisalsDao")
	private UnitAppraisalsDao unitAppraisalsDao;
	@Scheduled(cron="0 30 2 * * ?")   //ÿ�����ִ��
	public void score() {
		unitAppraisalsDao.score();
	}
	@Override
	public void update(UnitAppraisals unitAppraisals) {
		unitAppraisalsDao.save(unitAppraisals);
	}
	@Override
	public void save(UnitAppraisals unitAppraisals) {
		unitAppraisalsDao.update(unitAppraisals);
	}
	//���ݲ��ű�ź����ڲ�ѯʵ��
	@Override
	public UnitAppraisals queryByMany(String departmentSn, LocalDate localDate,Byte type) {
		return unitAppraisalsDao.queryByMany(departmentSn, localDate,type);
	}
	//���ݲ��ű�ţ���ݷݻ�ȡ�ò���һ��������·�ƽ����
	@Override
	public JSONArray queryScore(String year, String departmentTypeSn,Byte type){
		return unitAppraisalsDao.queryScore(year, departmentTypeSn,type);
	}
	//������ݺ��·ݺͲ��ű�Ų�ѯ�ò��Ÿ��µĵ÷�
	@Override
	public List<Object> queryMonth(String year, String month, String departmentSn,Byte type) {
		return unitAppraisalsDao.queryMonth(year, month, departmentSn,type);
	}
	//�鿴�۷�����
	@Override
	public JSONArray scoreDetail(String departmentSn, LocalDate localdate,String ksDate,String jsDate,Byte type) {
		return unitAppraisalsDao.scoreDetail(departmentSn, localdate,ksDate,jsDate,type);
	}
	@Override
	public List<UnitAppraisals> queryByDepartmentDate(String departmentSn, String ksDate, String jsDate) {
		// TODO Auto-generated method stub
		return unitAppraisalsDao.queryByDepartmentDate(departmentSn,ksDate, jsDate);
	}
	
	//��ѯĳһ��۷�����
	@Override
	public JSONObject socoreByDay(String departmentSn, LocalDate local,Byte type) {
		return unitAppraisalsDao.socoreByDay(departmentSn, local,type);
	}
	
	//�������������µ�ĳ�¿��˴��˵��
	@Override
	public InputStream exportMonthByDepartmentTypeSn(String departmentTypeSn, String departmentTypeName,
			String year,String month,Byte type) {
		return unitAppraisalsDao.exportMonthByDepartmentTypeSn(departmentTypeSn, departmentTypeName, year, month,type);
	}
	
	//����ĳ�����µ�ĳ�¿��˴��˵��
	@Override
	public InputStream exportMonthByDepartmentSn(String departmentSn, String departmentName, String year,
			String month,Byte type) {
		return unitAppraisalsDao.exportMonthByDepartmentSn(departmentSn, departmentName, year, month,type);
	}
	
	/**
	 * @method ��ȫ������������
	 * @author mahui
	 * @return String
	 */
	@Override
	public InputStream exportUnsafeCondition(String departmentSn, LocalDate localdate) {
		// TODO Auto-generated method stub
		return unitAppraisalsDao.exportUnsafeCondition(departmentSn, localdate);
	}
}
