package cn.jagl.aq.dao;

import java.io.InputStream;
import java.sql.Date;
import cn.jagl.aq.common.dao.BaseDao;
import cn.jagl.aq.domain.SuperviseDailyReport;
import net.sf.json.JSONObject;
public interface SuperviseDailyReportDao extends BaseDao<SuperviseDailyReport> {


	SuperviseDailyReport getSuperviseDailyReportByDateandSn(Date reportDate, String departmentTypeSn);

	void updateDailyReport(SuperviseDailyReport superviseDailyReport);
	
	//根据部门编号，年月获取个数
	long countByMany(String departmentSn,Date startDate,Date endDate);
	
	//分页查询
	JSONObject getByPage(int page, int rows, Date startDate, Date endDate, String departmentTypeSn);
	
	//生成日报文件并上传	
	void upload();
	
	//预览当日
	InputStream online(Date reportDate,String departmentTypeSn,String type);
}
