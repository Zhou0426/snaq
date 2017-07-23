package cn.jagl.aq.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import cn.jagl.aq.domain.UnitAppraisals;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��8��17������6:14:51
 */
public interface UnitAppraisalsService {
	//��ʱ����÷�
	public void score();
	//����
	public void update(UnitAppraisals unitAppraisals);
	//����
	public void save(UnitAppraisals unitAppraisals);
	//���ݲ��ű�ź����ڲ�ѯʵ��
	public UnitAppraisals queryByMany(String departmentSn, LocalDate localDate,Byte type);
	//���ݲ��ű�ţ���ݷݻ�ȡ�ò���һ��������·�ƽ����
	public JSONArray queryScore(String year, String departmentTypeSn,Byte type);
	//������ݺ��·ݺͲ��ű�Ų�ѯ�ò��Ÿ��µĵ÷�
	public List<Object> queryMonth(String year,String month,String departmentSn,Byte type);
	//�鿴�۷�����
	public JSONArray scoreDetail(String departmentSn, LocalDate localdate,String ksDate,String jsDate,Byte type);
	//���ݲ��ű�ź͵��²�ѯʵ�弯��--���ҿ���
	public List<UnitAppraisals> queryByDepartmentDate(String departmentSn, String ksDate, String jsDate);
	
	//��ѯĳһ��۷�����
	public JSONObject socoreByDay(String departmentSn,LocalDate local,Byte type);
	
	//�������������µ�ĳ�¿��˴��˵��
	public InputStream exportMonthByDepartmentTypeSn(String departmentTypeSn,String departmentTypeName,String year,String month,Byte type);

	//����ĳ�����µ�ĳ�¿��˴��˵��
	public InputStream exportMonthByDepartmentSn(String departmentSn,String departmentName,String year,String month,Byte type);
	
	/**
	 * @method ��ȫ������������
	 * @author mahui
	 * @return String
	 */
	InputStream exportUnsafeCondition(String departmentSn,LocalDate localdate);
}
