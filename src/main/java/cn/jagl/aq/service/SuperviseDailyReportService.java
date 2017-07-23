package cn.jagl.aq.service;

import java.io.InputStream;
import java.sql.Date;
import cn.jagl.aq.domain.SuperviseDailyReport;
import net.sf.json.JSONObject;

public interface SuperviseDailyReportService {
	SuperviseDailyReport getSuperviseDailyReportByDateandSn(Date reportDate, String departmentTypeSn);

	void updateDailyReport(SuperviseDailyReport superviseDailyReport);
	
	//save
	void save(SuperviseDailyReport superviseDailyReport);

	//���ݲ��ű�ţ����»�ȡ����
	long countByMany(String departmentSn,Date startDate,Date endDate);
	
	//��ҳ��ѯ
	JSONObject getByPage(int page, int rows, Date startDate, Date endDate, String departmentTypeSn);
	
	//ÿ���ձ��ϴ�
	void upload();
	
	//�鿴����
	InputStream online(Date reportDate,String departmentTypeSn,String type);
}
