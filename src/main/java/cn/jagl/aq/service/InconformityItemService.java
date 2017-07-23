package cn.jagl.aq.service;

import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface InconformityItemService {
	//���ӿ��˲�ѯ��굥λ�µ���������
	public List<Object> queryAllDepartmentType(String departmentSn);
	//���ӿ��˲�ѯ����
	public JSONArray query(String departmentSn, String departmentTypeSn, String year, String month);
	//���ӿ��˲�ѯ��������
	public JSONObject queryUnsafeCondition(String departmentSn,String year,String month,String type,String value,int page,int rows);
	//���ӿ��˲�ѯ����ȫ��Ϊ����
	public JSONObject queryUnsafeAct(String departmentSn,String year,String month,String value,int page,int rows);

	//���ӿ��˲�ѯ��������
	public InputStream exportUnsafeCondition(String departmentSn,String year,String month,String type,String value);

	//���ӿ��˲�ѯ����ȫ��Ϊ����
	public InputStream exportUnsafeAct(String departmentSn,String year,String month,String value);
}
