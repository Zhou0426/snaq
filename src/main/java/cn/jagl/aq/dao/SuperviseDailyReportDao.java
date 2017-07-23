package cn.jagl.aq.dao;

import java.io.InputStream;
import java.sql.Date;
import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SuperviseDailyReport;
import net.sf.json.JSONObject;
public interface SuperviseDailyReportDao extends BaseDao<SuperviseDailyReport> {


	SuperviseDailyReport getSuperviseDailyReportByDateandSn(Date reportDate, String departmentTypeSn);

	void updateDailyReport(SuperviseDailyReport superviseDailyReport);
	
	//���ݲ��ű�ţ����»�ȡ����
	long countByMany(String departmentSn,Date startDate,Date endDate);
	
	//��ҳ��ѯ
	JSONObject getByPage(int page, int rows, Date startDate, Date endDate, String departmentTypeSn);
	
	//�����ձ��ļ����ϴ�	
	void upload();
	
	//Ԥ������
	InputStream online(Date reportDate,String departmentTypeSn,String type);
}
