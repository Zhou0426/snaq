package cn.jagl.aq.service;

import cn.jagl.aq.domain.SuperviseDailyReportDetails;

public interface SuperviseDailyReportDetailsService {

	public void updateSuperviseDailyReportDetails(SuperviseDailyReportDetails superviseDailyReportDetails);
	
	//�����ձ�id�����š�item��Ų�ѯ
	public SuperviseDailyReportDetails getByMany(int id, String departmentSn,String superviseItemSn);
	
	//���
	public void save(SuperviseDailyReportDetails superviseDailyReportDetails);
	
}
