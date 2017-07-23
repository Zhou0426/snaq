package cn.jagl.aq.dao.impl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.StandardIndexAuditMethodDao;
import cn.jagl.aq.dao.SystemAuditDao;
import cn.jagl.aq.dao.SystemAuditScoreDao;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.SystemAudit;
import cn.jagl.aq.domain.SystemAuditScore;
import cn.jagl.aq.domain.UnsafeCondition;

/**
 * @author mahui
 * @method 
 * @date 2016年7月25日下午12:49:32
 */
@Repository("systemAuditDao")
public class SystemAuditDaoImpl extends BaseDaoHibernate5<SystemAudit> implements SystemAuditDao {

	@Resource(name="systemAuditScoreDao")
	private SystemAuditScoreDao systemAuditScoreDao;
	@Resource(name="standardIndexAuditMethodDao")
	private StandardIndexAuditMethodDao standardIndexAuditMethodDao;
	@Override
	public long count(String hql) {
		return (long) getSessionFactory().getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public void deleteByIds(String ids) {
		String hql="update SystemAudit s SET s.deleted=true WHERE s.id in ("+ids+")";
		getSessionFactory().getCurrentSession().createQuery(hql).executeUpdate();
	}

	//根据部门编号获取季度审核得分
	@Override
	public SystemAudit queryScore(String departmentSn,int quarter,String year){
		String sql="SELECT system_audit.* FROM system_audit WHERE deleted=false AND Year(check_quarter)="+year+" AND QUARTER(check_quarter)="+quarter+" and department_sn="+departmentSn+" AND audit_type=1 limit 1";
		return (SystemAudit) getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(SystemAudit.class).uniqueResult();
	}
	
	//根据部门编号获取神华审核得分
	@Override
	public SystemAudit queryShScore(String departmentSn,String year){
		String sql="SELECT system_audit.* FROM system_audit WHERE deleted=false AND Year(start_date)="+year+" and department_sn="+departmentSn+" AND audit_type=2 limit 1";
		return (SystemAudit) getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(SystemAudit.class).uniqueResult();
	}
	//getBySn;
	public SystemAudit getBySn(String auditSn){
		String hql="SELECT s from SystemAudit s WHERE s.auditSn=:auditSn";
		return (SystemAudit) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("auditSn", auditSn)
					.uniqueResult();
	}

	//获取无需扣扣的分数
	@Override
	public int countNotScore(String auditSn) {
		String sql="select sum(s.integer_score) from standard_index s INNER JOIN system_audit_not_scored_index n on s.index_sn=n.index_sn where n.audit_sn="+auditSn+" and not EXISTS(select * from system_audit_not_scored_index where audit_sn="+auditSn+" and n.index_sn<>index_sn and LOCATE(CONCAT(n.index_sn,'.'),index_sn)>0)";
		Object o=getSessionFactory().getCurrentSession()
				.createSQLQuery(sql)
				.uniqueResult();
		if(o==null){
			return 0;
		}
		return ((BigDecimal)o).intValue();
	}
	//获取某指标无需扣扣的分数
	@Override
	public int countNotScoreBySn(String auditSn, String indexSn) {
		String sql="select sum(s.integer_score) from standard_index s INNER JOIN system_audit_not_scored_index n on s.index_sn=n.index_sn where (s.index_sn='"+indexSn+"' or s.index_sn like '"+indexSn+".%') and n.audit_sn="+auditSn+" and not EXISTS(select * from system_audit_not_scored_index where audit_sn="+auditSn+" and n.index_sn<>index_sn and LOCATE(CONCAT(n.index_sn,'.'),index_sn)>0)";
		Object o=getSessionFactory().getCurrentSession()
				.createSQLQuery(sql)
				.uniqueResult();
		if(o==null){
			return 0;
		}
		return ((BigDecimal)o).intValue();
	}

	//判断是否全选
	@Override
	public boolean isChecked(String auditSn, String indexSn) {
		String sql1="select count(*) from standard_index s where s.deleted=false and s.integer_score is not null and s.index_sn like '"+indexSn+".%'";
		String sql2="select count(*) from system_audit_not_scored_index s where audit_sn="+auditSn+" and index_sn like '"+indexSn+".%'";
		String sql3="select count(*) from system_audit_not_scored_index s where audit_sn="+auditSn+" and index_sn ='"+indexSn+"'";
		Long s1=((BigInteger) getSessionFactory().getCurrentSession()
				.createSQLQuery(sql1)
				.uniqueResult()).longValue();
		Long s2=((BigInteger) getSessionFactory().getCurrentSession()
				.createSQLQuery(sql2)
				.uniqueResult()).longValue();
		
		boolean b=false;
		if(s1!=0){
			if(s1==s2){
				b=true;
			}
		}else{
			Long s3=((BigInteger) getSessionFactory().getCurrentSession()
					.createSQLQuery(sql3)
					.uniqueResult()).longValue();
			if(s3!=0){
				b=true;
			}
		}
		return b;
	}

	//根据指标编号和审核编号判断是否应该打分
	@Override
	public boolean isScore(String auditSn, String indexSn) {
		String sql="select count(*) from system_audit_not_scored_index s where audit_sn="+auditSn+" and index_sn ='"+indexSn+"'";
		boolean b=true;
		Long s=((BigInteger) getSessionFactory().getCurrentSession()
				.createSQLQuery(sql)
				.uniqueResult()).longValue();
		if(s!=0){
			b=false;
		}
		return b;
	}

	//输出审核打分表
	@SuppressWarnings("unchecked")
	@Override
	public InputStream export(String standardSn, String auditSn) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet1 = wb.createSheet("评分表");
		HSSFSheet sheet2 = wb.createSheet("汇总表");
		
		//列宽设置
		sheet1.setColumnWidth(0,60*60);
        sheet1.setColumnWidth(1,80*80);
        sheet1.setColumnWidth(2, 60*60);
        sheet1.setColumnWidth(3, 50*50);
        sheet1.setColumnWidth(4, 50*50);
        sheet1.setColumnWidth(5, 50*50);
        sheet1.setColumnWidth(6, 60*60);
        sheet1.setColumnWidth(7, 60*60);
        
        sheet2.setColumnWidth(0,50*50);
        sheet2.setColumnWidth(1,90*90);
        sheet2.setColumnWidth(2, 60*60);
        sheet2.setColumnWidth(3, 60*60);
        sheet2.setColumnWidth(4, 60*60);
		//样式一（sheet1标题）
		HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("黑体");
        font1.setFontHeightInPoints((short) 20);
        style1.setFont(font1);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        //样式2（表头）
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 12);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setWrapText(true);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette = wb.getCustomPalette();   
        palette.setColorAtIndex((short)9, (byte) 128, (byte) 128, (byte) 0);
        style2.setFillForegroundColor((short)9);
        //style2.setFillBackgroundColor((short)9);
        
        //样式三（深绿背景）
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName("宋体");
        font3.setFontHeightInPoints((short) 12);
        style3.setFont(font3);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setWrapText(true);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette2 = wb.getCustomPalette();
        palette2.setColorAtIndex((short)10, (byte) 51, (byte) 153, (byte) 102);
        style3.setFillForegroundColor((short)10);
        
        //深绿样式九（深绿背景，加粗）
        HSSFCellStyle style9 = wb.createCellStyle();
        HSSFFont font6 = wb.createFont();
        font6.setFontName("宋体");
        font6.setFontHeightInPoints((short) 12);
        font6.setBold(true);
        style9.setFont(font6);
        style9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style9.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style9.setBorderBottom(BorderStyle.THIN);
        style9.setBorderLeft(BorderStyle.THIN);
        style9.setBorderTop(BorderStyle.THIN);
        style9.setBorderRight(BorderStyle.THIN);
        style9.setWrapText(true);
        style9.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style9.setFillForegroundColor((short)10);
        
        
        //样式四（浅绿背景）
        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font3);
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setBorderTop(BorderStyle.THIN);
        style4.setBorderRight(BorderStyle.THIN);
        style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
        style4.setWrapText(true);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette3 = wb.getCustomPalette();
        palette3.setColorAtIndex((short)11, (byte) 204, (byte) 255, (byte) 204);
        style4.setFillForegroundColor((short)11);
        
        
        //样式五（粉红背景,小数两位）
        HSSFCellStyle style5 = wb.createCellStyle();
        style5.setFont(font3);
        style5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style5.setBorderBottom(BorderStyle.THIN);
        style5.setBorderLeft(BorderStyle.THIN);
        style5.setBorderTop(BorderStyle.THIN);
        style5.setBorderRight(BorderStyle.THIN);
        style5.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        style5.setWrapText(true);
        style5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette4 = wb.getCustomPalette();
        palette4.setColorAtIndex((short)12, (byte) 255, (byte) 153, (byte) 204);
        style5.setFillForegroundColor((short)12);
        
        //样式八（粉红背景,百分比）
        HSSFCellStyle style8 = wb.createCellStyle();
        style8.setFont(font3);
        style8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style8.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style8.setBorderBottom(BorderStyle.THIN);
        style8.setBorderLeft(BorderStyle.THIN);
        style8.setBorderTop(BorderStyle.THIN);
        style8.setBorderRight(BorderStyle.THIN);
        style8.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
        style8.setWrapText(true);
        style8.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette5 = wb.getCustomPalette();
        palette5.setColorAtIndex((short)12, (byte) 255, (byte) 153, (byte) 204);
        style8.setFillForegroundColor((short)12);
        //样式六（无背景色）
        HSSFCellStyle style6 = wb.createCellStyle();
        style6.setFont(font3);
        style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style6.setBorderBottom(BorderStyle.THIN);
        style6.setBorderLeft(BorderStyle.THIN);
        style6.setBorderTop(BorderStyle.THIN);
        style6.setBorderRight(BorderStyle.THIN);
        style6.setWrapText(true);
        
        //样式七（sheet2标题）
        HSSFCellStyle style7 = wb.createCellStyle();
        HSSFFont font7 = wb.createFont();
        font7.setFontName("黑体");
        font7.setFontHeightInPoints((short) 16);
        style7.setFont(font7);
        style7.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style7.setBorderBottom(BorderStyle.THIN);
        style7.setBorderLeft(BorderStyle.THIN);
        style7.setBorderTop(BorderStyle.THIN);
        style7.setBorderRight(BorderStyle.THIN);
        //产生row和cell
        HSSFRow row1;
        HSSFRow row2;
        HSSFCell cell1;
        HSSFCell cell2;
        
        //产生标题
        row1 = sheet1.createRow(0);
        row1.setHeight((short) (25*25));
        row2 = sheet2.createRow(0);
        row2.setHeight((short) (25*25));
        cell1=row1.createCell(0);
        cell1.setCellValue("神华集团安全风险预控管理体系审核评分表");
        cell1.setCellStyle(style1);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        for(int i=1;i<8;i++){
        	cell1=row1.createCell(i);
        	cell1.setCellStyle(style1);
        }
        
        cell2=row2.createCell(0);
        cell2.setCellValue("神华集团公司安全风险预控管理体系考核直接得分");
        cell2.setCellStyle(style7);
        sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        for(int i=1;i<5;i++){
        	cell2=row2.createCell(i);
        	cell2.setCellStyle(style7);
        }
        
        //生成表头
        row1 = sheet1.createRow(1);
        row1.setHeight((short) (25*25));
        row2 = sheet2.createRow(1);
        row2.setHeight((short) (25*25));
        cell1=row1.createCell(0);
        cell1.setCellValue("要素");
        cell1.setCellStyle(style2);
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
        cell1=row1.createCell(1);
        cell1.setCellStyle(style2);
        cell1=row1.createCell(2);
        cell1.setCellValue("环节设置分值（N）");
        cell1.setCellStyle(style2);
        cell1=row1.createCell(3);
        cell1.setCellValue("环节");
        cell1.setCellStyle(style2);
        cell1=row1.createCell(4);
        cell1.setCellValue("符合度");
        cell1.setCellStyle(style2);
        cell1=row1.createCell(5);
        cell1.setCellValue("环节得分");
        cell1.setCellStyle(style2);
        cell1=row1.createCell(6);
        cell1.setCellValue("要素得分");
        cell1.setCellStyle(style2);
        cell1=row1.createCell(7);
        cell1.setCellValue("备注");
        cell1.setCellStyle(style2);
        
        cell2=row2.createCell(0);
        cell2.setCellValue("元素号");
        cell2.setCellStyle(style2);
        
        cell2=row2.createCell(1);
        cell2.setCellValue("元素名");
        cell2.setCellStyle(style2);
        
        cell2=row2.createCell(2);
        cell2.setCellValue("设计分");
        cell2.setCellStyle(style2);
        
        cell2=row2.createCell(3);
        cell2.setCellValue("实得分");
        cell2.setCellStyle(style2);
        
        cell2=row2.createCell(4);
        cell2.setCellValue("得分率");
        cell2.setCellStyle(style2);
        
        //冻结行
        sheet1.createFreezePane(0, 2, 0, 2);
        sheet2.createFreezePane(0, 2, 0, 2);
        
        //获取所有父级
        String hql="select s FROM StandardIndex s INNER JOIN s.standard d where s.parent is null and s.deleted=false and d.standardSn =:standardSn order by s.showSequence asc";
        
		List<StandardIndex> parents=(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql)
        		.setString("standardSn", standardSn).list();
		int sheet1row=1;
		int sheet2row=1;
		int allscore=0;//指标总分
		float getscore=0;//所有分
		for(StandardIndex parent:parents){
			//筛选所有参与打分项
			float score=0;//顶级指标总得分
			int startRow=sheet1row+1;
			int topscore=0;//顶级总分
			boolean first=isChecked(auditSn, parent.getIndexSn());
			//if(!isChecked(auditSn, parent.getIndexSn())){
				//sheet2表格内容
				sheet2row++;
				row2=sheet2.createRow(sheet2row);
				row2.setHeight((short) (20*20));
				cell2=row2.createCell(0);
				if(parent.getIndexSn().split("-").length>1){
					cell2.setCellValue(parent.getIndexSn().split("-")[1]);
				}else{
					cell2.setCellValue(parent.getIndexSn());
				}
				cell2.setCellStyle(style3);
				cell2=row2.createCell(1);
				cell2.setCellValue(parent.getIndexName());
				cell2.setCellStyle(style3);
				cell2=row2.createCell(2);
				if(parent.getIntegerScore()!=null && !first){
					topscore=parent.getIntegerScore()-countNotScoreBySn(auditSn, parent.getIndexSn());
				}
				cell2.setCellValue(topscore);
				cell2.setCellStyle(style3);
				
				//sheet1
				String hql2="select s from StandardIndex s where s.deleted=false and s.integerScore is not null and s.standard.standardSn=:standardSn and s.parent.indexSn=:indexSn order by s.showSequence";
				List<StandardIndex> child11 =(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql2)		
						.setString("indexSn", parent.getIndexSn()).setString("standardSn", standardSn).list();
				if(child11!=null&&child11.size()>0){
					//多于两层
					for(StandardIndex index11:child11){
						boolean second=isChecked(auditSn, index11.getIndexSn());
						//if(!isChecked(auditSn, index11.getIndexSn())){
							String hql3="select s from StandardIndex s where s.deleted=false and s.integerScore is not null and s.standard.standardSn=:standardSn and s.parent.indexSn=:indexSn order by s.showSequence";
							List<StandardIndex> child21 =(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql3)		
									.setString("indexSn", index11.getIndexSn()).setString("standardSn", standardSn).list();							
							if(child21!=null && child21.size()>0){
								//多于三层
								for(StandardIndex index21:child21){
									boolean third=isChecked(auditSn, index21.getIndexSn());
									//if(!isChecked(auditSn, index21.getIndexSn())){
										String hql4="select s from StandardIndex s where s.deleted=false and s.integerScore is not null and s.standard.standardSn=:standardSn and s.parent.indexSn=:indexSn order by s.showSequence";
										List<StandardIndex> child31 =(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql4)		
												.setString("indexSn", index21.getIndexSn()).setString("standardSn", standardSn).list();							
										if(child31!=null && child31.size()>0){
											//多于四层
											
										}else{
											//四层
											float partScore=0;//环节得分；
											int partStart=sheet1row+1;
											String hql5="select s from StandardIndex s where s.deleted=false and s.integerScore is null and s.standard.standardSn=:standardSn and s.parent.indexSn=:indexSn order by s.showSequence";
											List<StandardIndex> child32 =(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql5)		
													.setString("indexSn", index21.getIndexSn()).setString("standardSn", standardSn).list();
											for(StandardIndex index32:child32){
												sheet1row++;
												row1=sheet1.createRow(sheet1row);
												row1.setHeight((short) (20*20));
												cell1=row1.createCell(0);
												if(parent.getIndexSn().split("-").length>1){
													String name=parent.getIndexSn().split("-")[1]+"\r"+parent.getIndexName();
													cell1.setCellValue(name);
												}else{
													String name=parent.getIndexSn().split("-")[1]+"\r"+parent.getIndexName();
													cell1.setCellValue(name);
												}
												cell1.setCellStyle(style3);
												
												cell1=row1.createCell(1);
												if(index21.getIndexSn().split("-").length>1){
													cell1.setCellValue(index21.getIndexSn().split("-")[1]+index21.getIndexName());
												}else{
													cell1.setCellValue(index21.getIndexSn()+index21.getIndexName());
												}
												cell1.setCellStyle(style3);
												
												if(first || second || third){
													cell1=row1.createCell(2);
													cell1.setCellValue(0);
													cell1.setCellStyle(style3);
													
													cell1=row1.createCell(4);
													cell1.setCellValue(1);
													cell1.setCellStyle(style4);
													cell1=row1.createCell(5);
													cell1.setCellValue(0);
													cell1.setCellStyle(style5);
													
												}else{
													cell1=row1.createCell(2);
													cell1.setCellValue(index21.getIntegerScore());
													cell1.setCellStyle(style3);
													
													cell1=row1.createCell(4);
													SystemAuditScore systemAuditScore=systemAuditScoreDao.getByMany(auditSn,index32.getIndexSn());
													int com=100;
													if(systemAuditScore!=null){
														cell1.setCellValue((float)systemAuditScore.getConformDegree()/100.00);
														com=systemAuditScore.getConformDegree();
													}else{
														cell1.setCellValue(1);
													}
													cell1.setCellStyle(style4);
													
													cell1=row1.createCell(5);
													int allScore=index21.getIntegerScore();
													float singleScore=0;
													switch (index32.getIndexSn().substring(index32.getIndexSn().trim().length()-1)){
													case "P":
														singleScore=(float) (allScore*0.2*com*0.01);
														partScore+=allScore*0.2*com*0.01;
														break;
													case "C":
														singleScore=(float) (allScore*0.2*com*0.01);
														partScore+=allScore*0.2*com*0.01;
														break;
													case "D":
														singleScore=(float) (allScore*0.5*com*0.01);
														partScore+=allScore*0.5*com*0.01;
														break;
													case "A":
														singleScore=(float) (allScore*0.1*com*0.01);
														partScore+=allScore*0.1*com*0.01;
														break;
													}
													cell1.setCellValue(singleScore);
													cell1.setCellStyle(style5);
												}
												
												
												cell1=row1.createCell(3);
												cell1.setCellValue(index32.getIndexSn().substring(index32.getIndexSn().trim().length()-1));
												cell1.setCellStyle(style9);
												
												cell1=row1.createCell(7);
												cell1.setCellStyle(style6);
												
												cell1=row1.createCell(6);
												cell1.setCellStyle(style5);
											}
											row1=sheet1.getRow(partStart);
											cell1=row1.getCell(6);
											cell1.setCellValue(partScore);
											
											//合并单元格
											if(partStart<sheet1row){
												//sheet1.addMergedRegion(new CellRangeAddress(startRow,sheet1row,0,0));
												sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,1,1));
												sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,2,2));
												sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,6,6));
												sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,7,7));
											}
											score+=partScore;
											
										}
									//}
								}
							}else{
								//只有三层
								float partScore=0;//环节得分；
								int partStart=sheet1row+1;
								String hql4="select s from StandardIndex s where s.deleted=false and s.integerScore is null and s.standard.standardSn=:standardSn and s.parent.indexSn=:indexSn order by s.showSequence";
								List<StandardIndex> child22 =(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql4)		
										.setString("indexSn", index11.getIndexSn()).setString("standardSn", standardSn).list();
								for(StandardIndex index22:child22){
									sheet1row++;
									row1=sheet1.createRow(sheet1row);
									row1.setHeight((short) (20*20));
									cell1=row1.createCell(0);
									if(parent.getIndexSn().split("-").length>1){
										cell1.setCellValue(parent.getIndexSn().split("-")[1]+"\r"+parent.getIndexName());
									}else{
										cell1.setCellValue(parent.getIndexSn()+"\r"+parent.getIndexName());
									}
									cell1.setCellStyle(style3);
									
									cell1=row1.createCell(1);
									if(index11.getIndexSn().split("-").length>1){
										cell1.setCellValue(index11.getIndexSn().split("-")[1]+index11.getIndexName());
									}else{
										cell1.setCellValue(index11.getIndexSn()+index11.getIndexName());
									}
									cell1.setCellStyle(style3);
									
									if(first || second){
										cell1=row1.createCell(2);
										cell1.setCellValue(0);
										cell1.setCellStyle(style3);
										
										cell1=row1.createCell(4);
										cell1.setCellValue(0);
										cell1.setCellStyle(style4);
										
										cell1=row1.createCell(5);
										cell1.setCellValue(0);
										cell1.setCellStyle(style5);
									}else{
										cell1=row1.createCell(2);
										cell1.setCellValue(index11.getIntegerScore());
										cell1.setCellStyle(style3);
										
										cell1=row1.createCell(4);
										SystemAuditScore systemAuditScore=systemAuditScoreDao.getByMany(auditSn,index22.getIndexSn());
										int com=100;
										if(systemAuditScore!=null){
											cell1.setCellValue((float)systemAuditScore.getConformDegree()/100.00);
											com=systemAuditScore.getConformDegree();
										}else{
											cell1.setCellValue(1);
										}
										cell1.setCellStyle(style4);
										
										cell1=row1.createCell(5);
										int allScore=index11.getIntegerScore();
										float singleScore=0;
										switch (index22.getIndexSn().substring(index22.getIndexSn().trim().length()-1)){
										case "P":
											singleScore=(float) (allScore*0.2*com*0.01);
											partScore+=allScore*0.2*com*0.01;
											break;
										case "C":
											singleScore=(float) (allScore*0.2*com*0.01);
											partScore+=allScore*0.2*com*0.01;
											break;
										case "D":
											singleScore=(float) (allScore*0.5*com*0.01);
											partScore+=allScore*0.5*com*0.01;
											break;
										case "A":
											singleScore=(float) (allScore*0.1*com*0.01);
											partScore+=allScore*0.1*com*0.01;
											break;
										}
										cell1.setCellValue(singleScore);
										cell1.setCellStyle(style5);
									}
									
									
									cell1=row1.createCell(3);
									cell1.setCellValue(index22.getIndexSn().substring(index22.getIndexSn().trim().length()-1));
									cell1.setCellStyle(style9);
									
									
									
									cell1=row1.createCell(7);
									cell1.setCellStyle(style6);
									
									cell1=row1.createCell(6);
									cell1.setCellStyle(style5);
								}
								row1=sheet1.getRow(partStart);
								cell1=row1.getCell(6);
								cell1.setCellValue(partScore);
								
								//合并单元格
								if(partStart<sheet1row){
									//sheet1.addMergedRegion(new CellRangeAddress(startRow,sheet1row,0,0));
									sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,1,1));
									sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,2,2));
									sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,6,6));
									sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,7,7));
								}
								score+=partScore;
							}
						//}
						
					}
					if(startRow<sheet1row){
						sheet1.addMergedRegion(new CellRangeAddress(startRow,sheet1row,0,0));
					}
					cell2=row2.createCell(3);
					cell2.setCellStyle(style5);
					cell2.setCellValue(score);
					
					cell2=row2.createCell(4);
					cell2.setCellStyle(style8);
					cell2.setCellValue(score/parent.getIntegerScore());
					
				}else{
					//只有两层（直接pcda）
					float partScore=0;//环节得分；
					int partStart=sheet1row+1;
					String hql3="select s from StandardIndex s where s.deleted=false and s.integerScore is null and s.standard.standardSn=:standardSn and s.parent.indexSn=:indexSn order by s.showSequence";
					List<StandardIndex> child12 =(List<StandardIndex>)getSessionFactory().getCurrentSession().createQuery(hql3)		
							.setString("indexSn", parent.getIndexSn()).setString("standardSn", standardSn).list();
					for(StandardIndex index12:child12){
						sheet1row++;
						row1=sheet1.createRow(sheet1row);
						row1.setHeight((short) (20*20));
						cell1=row1.createCell(0);
						if(parent.getIndexSn().split("-").length>1){
							cell1.setCellValue(parent.getIndexSn().split("-")[1]);
						}else{
							cell1.setCellValue(parent.getIndexSn());
						}
						cell1.setCellStyle(style3);
						
						cell1=row1.createCell(1);
						cell1.setCellValue(parent.getIndexName());
						cell1.setCellStyle(style3);
						if(first){
							cell1=row1.createCell(2);
							cell1.setCellValue(0);
							cell1.setCellStyle(style3);
							
							cell1=row1.createCell(4);
							cell1.setCellValue(0);
							cell1.setCellStyle(style4);
							
							cell1=row1.createCell(5);
							cell1.setCellValue(0);
							cell1.setCellStyle(style5);
							
						}else{
							cell1=row1.createCell(2);
							cell1.setCellValue(parent.getIntegerScore());
							cell1.setCellStyle(style3);
							
							cell1=row1.createCell(4);
							SystemAuditScore systemAuditScore=systemAuditScoreDao.getByMany(auditSn,index12.getIndexSn());
							int com=100;
							if(systemAuditScore!=null){
								cell1.setCellValue((float)systemAuditScore.getConformDegree()/100.00);
								com=systemAuditScore.getConformDegree();
							}else{
								cell1.setCellValue(1);
							}
							cell1.setCellStyle(style4);
							
							cell1=row1.createCell(5);
							int allScore=parent.getIntegerScore();
							float singleScore=0;
							switch (index12.getIndexSn().substring(index12.getIndexSn().trim().length()-1)){
							case "P":
								singleScore=(float) (allScore*0.2*com*0.01);
								partScore+=allScore*0.2*com*0.01;
								break;
							case "C":
								singleScore=(float) (allScore*0.2*com*0.01);
								partScore+=allScore*0.2*com*0.01;
								break;
							case "D":
								singleScore=(float) (allScore*0.5*com*0.01);
								partScore+=allScore*0.5*com*0.01;
								break;
							case "A":
								singleScore=(float) (allScore*0.1*com*0.01);
								partScore+=allScore*0.1*com*0.01;
								break;
							}
							cell1.setCellValue(singleScore);
							cell1.setCellStyle(style5);
						}
						
						
						cell1=row1.createCell(3);
						cell1.setCellValue(index12.getIndexSn().substring(index12.getIndexSn().trim().length()-1));
						cell1.setCellStyle(style9);
						
						
						cell1=row1.createCell(7);
						cell1.setCellStyle(style6);
						
						cell1=row1.createCell(6);
						cell1.setCellStyle(style5);
					}
					row1=sheet1.getRow(partStart);
					cell1=row1.getCell(6);
					cell1.setCellValue(partScore);
					//合并单元格
					if(startRow<sheet1row){
						sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,0,0));
						sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,1,1));
						sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,2,2));
						sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,6,6));
						sheet1.addMergedRegion(new CellRangeAddress(partStart,sheet1row,7,7));
					}
					score+=partScore;
					
					cell2=row2.createCell(3);
					cell2.setCellStyle(style5);
					cell2.setCellValue(score);
					
					cell2=row2.createCell(4);
					cell2.setCellStyle(style8);
					cell2.setCellValue(score/parent.getIntegerScore());
				}
				//
				if(sheet1row>2){
					row1=sheet1.getRow(sheet1row-1);
					if(row1!=null){
						cell1=row1.createCell(8);
						cell1.setCellValue(topscore);
						cell1.setCellStyle(style6);
					}
					row1=sheet1.getRow(sheet1row);
					if(row1!=null){
						cell1=row1.createCell(8);
						cell1.setCellValue(score);
						cell1.setCellStyle(style5);
					}
				}
				allscore+=topscore;//指标总分
				getscore+=score;//所有分
								
				
			//}
		}
		//表格尾部
		row1=sheet1.createRow(sheet1row+1);
		row1.setHeight((short) (20*20));
		for(int i=0;i<6;i++){
			cell1=row1.createCell(i);
			cell1.setCellStyle(style9);
		}
		
		cell1=row1.getCell(0);
		cell1.setCellValue("合计");
		cell1=row1.createCell(6);
		cell1.setCellValue(getscore);
		cell1.setCellStyle(style5);
		
		cell1=row1.createCell(7);
		cell1.setCellStyle(style6);
		
		sheet1.addMergedRegion(new CellRangeAddress(sheet1row+1,sheet1row+1,0,5));
		
		row2=sheet2.createRow(sheet2row+1);
		row2.setHeight((short) (20*20));
		for(int i=0;i<3;i++){
			cell2=row2.createCell(i);
			cell2.setCellStyle(style9);
		}
		cell2=row2.getCell(0);
		cell2.setCellValue("合计");
		cell2.setCellStyle(style9);
		
		cell2=row2.getCell(1);
		cell2.setCellStyle(style9);
		
		cell2=row2.getCell(2);
		cell2.setCellValue(allscore);
		cell2.setCellStyle(style3);
		
		cell2=row2.createCell(3);
		cell2.setCellValue(getscore);
		cell2.setCellStyle(style5);
		
		cell2=row2.createCell(4);
		if(allscore>0){
			cell2.setCellValue(getscore/allscore);
		}else{
			cell2.setCellValue(0);
		}
		cell2.setCellStyle(style8);
		sheet2.addMergedRegion(new CellRangeAddress(sheet2row+1,sheet2row+1,0,1));
        try{  
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
            wb.write(fout);
            wb.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
            return is;
        }catch (Exception e){  
            e.printStackTrace();  
            return null;
        }		
	}

	//输出各矿得分情况
	@SuppressWarnings("unchecked")
	@Override
	public InputStream exportSummary(String year, String month, int type, String standardSn, String departmentTypeSn) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("各矿得分情况");
		HSSFRow row;
		HSSFCell cell;
		sheet.setColumnWidth(0, 50*50);
		sheet.setColumnWidth(1, 90*90);
		sheet.setColumnWidth(2, 60*60);
		sheet.setColumnWidth(3, 60*60);
		sheet.setColumnWidth(4, 60*60);
		sheet.setColumnWidth(5, 60*60);
		sheet.setColumnWidth(6, 90*90);
		//样式一（表头）
		HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 16);
        font1.setBold(true);
        style1.setFont(font1);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setWrapText(true);
        
        //样式二（普通内容）
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 12);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setWrapText(true);
        //样式四（格式）
        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font2);
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setBorderTop(BorderStyle.THIN);
        style4.setBorderRight(BorderStyle.THIN);
        style4.setWrapText(true);
        style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        //样式三
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName("宋体");
        font3.setFontHeightInPoints((short) 12);
        style3.setFont(font3);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setWrapText(true);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFPalette palette = wb.getCustomPalette();   
        palette.setColorAtIndex((short)9, (byte) 150, (byte) 150, (byte) 150);
        style3.setFillForegroundColor((short)9);
        
        //生成标题
        row=sheet.createRow(0);
        row.setHeight((short) (30*30));
        cell=row.createCell(0);
        cell.setCellValue("安全风险预控管理体系审核各矿得分情况");
        cell.setCellStyle(style1);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        //生成表头
        row=sheet.createRow(1);
        row.setHeight((short) (25*25));
        cell=row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style3);
        
        cell=row.createCell(1);
        cell.setCellValue("单位");
        cell.setCellStyle(style3);
        
        cell=row.createCell(2);
        cell.setCellValue("设计分值");
        cell.setCellStyle(style3);
        
        cell=row.createCell(3);
        cell.setCellValue("实得分");
        cell.setCellStyle(style3);
        
        cell=row.createCell(4);
        cell.setCellValue("修正分");
        cell.setCellStyle(style3);
        
        cell=row.createCell(5);
        cell.setCellValue("换算分");
        cell.setCellStyle(style3);
        
        cell=row.createCell(6);
        cell.setCellValue("备注");
        cell.setCellStyle(style3);
        
        
		String hql="select s FROM SystemAudit s LEFT JOIN s.standard t WHERE s.deleted=false AND s.systemAuditType="+type;
		if(year!=null&&year.length()>0){
			hql+=" AND year(s.startDate) ="+year;
		}
		if(month!=null&&month.length()>0){
			hql+=" AND month(s.startDate) in ("+month+")";
		}
		if(standardSn!=null&&standardSn.length()>0){
			hql+=" AND t.standardSn like '"+standardSn+"'";
		}else if(departmentTypeSn!=null&&departmentTypeSn.length()>0){
			hql+=" AND t.departmentType.departmentTypeSn like '"+departmentTypeSn+"'";
		}
		List<SystemAudit> list=new ArrayList<SystemAudit>();
		list=(List<SystemAudit>)getSessionFactory().getCurrentSession().createQuery(hql).list();
		int i=0;
		for(SystemAudit systemAudit:list){
			i++;
			row=sheet.createRow(i+1);
			row.setHeight((short) (25*25));
			cell=row.createCell(0);
			cell.setCellValue(i);
			cell.setCellStyle(style2);
			
			cell=row.createCell(1);
			cell.setCellValue(systemAudit.getAuditedDepartment().getDepartmentName());
			cell.setCellStyle(style2);
			
			cell=row.createCell(2);
			if(systemAudit.getDesignPoints()!=null)
			cell.setCellValue(systemAudit.getDesignPoints());
			cell.setCellStyle(style2);
			
			cell=row.createCell(3);
			if(systemAudit.getRealScore()!=null)
			cell.setCellValue(systemAudit.getRealScore());
			cell.setCellStyle(style4);
			
			cell=row.createCell(4);
			if(systemAudit.getAmendScore()!=null)
			cell.setCellValue(systemAudit.getAmendScore());
			cell.setCellStyle(style2);
			
			cell=row.createCell(5);
			if(systemAudit.getFinalScore()!=null)
			cell.setCellValue(systemAudit.getFinalScore());
			cell.setCellStyle(style4);
			
			cell=row.createCell(6);
			if(systemAudit.getRemark()!=null)
			cell.setCellValue(systemAudit.getRemark());
			cell.setCellStyle(style2);
		}
		try{  
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
            wb.write(fout);
            wb.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
            return is;
        }catch (Exception e){  
            e.printStackTrace();  
            return null;
        }
	}

	/**
	 * @method 体系审核隐患导出
	 * @author mahui
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	public InputStream exportUnsafeCondition(String auditSn) {
		String hql="select h from UnsafeCondition h WHERE h.deleted=false and h.systemAudit.auditSn='"+auditSn+"' order by h.hasReviewed,h.hasCorrectConfirmed,h.inconformityItemSn desc";
		List<UnsafeCondition> list=getSessionFactory().getCurrentSession().createQuery(hql).list();
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("隐患");
		sheet.setDefaultColumnWidth(17);
		sheet.setColumnWidth(0, 70*70);
		sheet.setColumnWidth(5, 90*90);
		sheet.setColumnWidth(8, 70*70);
		sheet.setColumnWidth(9, 75*75);
		sheet.setColumnWidth(13, 90*90);
		//样式一（标题）
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 12);
        style1.setFont(font1);
        font1.setBold(true);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setWrapText(true);
        
        //样式二（内容）
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 11);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        
        //样式三日期
        HSSFDataFormat format = wb.createDataFormat();
        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font2);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setWrapText(true);
        style3.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm"));
        
        //生成表头
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("检查时间");  
        cell.setCellStyle(style1);  
        cell = row.createCell(1);  
        cell.setCellValue("检查地点");  
        cell.setCellStyle(style1);  
        cell = row.createCell(2);  
        cell.setCellValue("不符合项性质");  
        cell.setCellStyle(style1);
        cell = row.createCell(3);
        cell.setCellValue("机");  
        cell.setCellStyle(style1); 
        cell = row.createCell(4);
        cell.setCellValue("被检部门");  
        cell.setCellStyle(style1); 
        cell = row.createCell(5);
        cell.setCellValue("问题描述");  
        cell.setCellStyle(style1);
        cell = row.createCell(6);  
        cell.setCellValue("扣分");  
        cell.setCellStyle(style1); 
        cell = row.createCell(7);  
        cell.setCellValue("不符合项等级");  
        cell.setCellStyle(style1);
        cell = row.createCell(8);
        cell.setCellValue("整改期限");  
        cell.setCellStyle(style1);
        cell = row.createCell(9);
        cell.setCellValue("整改建议");  
        cell.setCellStyle(style1);
        cell = row.createCell(10);
        cell.setCellValue("整改确认");  
        cell.setCellStyle(style1);
        cell = row.createCell(11);
        cell.setCellValue("已复查");  
        cell.setCellStyle(style1);
        cell = row.createCell(12);
        cell.setCellValue("整改完成");  
        cell.setCellStyle(style1);
        cell = row.createCell(13);
        cell.setCellValue("扣分指标");  
        cell.setCellStyle(style1);
        cell = row.createCell(14);
        cell.setCellValue("危险源");  
        cell.setCellStyle(style1);
        cell = row.createCell(15);
        cell.setCellValue("扣分项");  
        cell.setCellStyle(style1);
        cell = row.createCell(16);
        cell.setCellValue("专业");  
        cell.setCellStyle(style1);
        cell = row.createCell(17);
        cell.setCellValue("检查人");  
        cell.setCellStyle(style1);
        cell = row.createCell(18);
        cell.setCellValue("整改负责人");  
        cell.setCellStyle(style1);
        cell = row.createCell(19);
        cell.setCellValue("录入人");  
        cell.setCellStyle(style1);
        
        int rownum=1;
		for(UnsafeCondition u:list){
			row=sheet.createRow(rownum);
			rownum++;
			//检查时间
	        cell=row.createCell(0);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCheckDateTime()!=null){
	        	cell.setCellValue(u.getCheckDateTime());  
	        }
	        
	        cell.setCellStyle(style3);  
	        
	        //检查地点
	        cell = row.createCell(1);  
	        cell.setCellValue(u.getCheckLocation());  
	        cell.setCellStyle(style2); 
	        
	        //不符合项性质
	        cell = row.createCell(2);  
	        cell.setCellValue(u.getInconformityItemNature()==null? "":u.getInconformityItemNature().toString());  
	        cell.setCellStyle(style2);
	        
	        //机
	        cell = row.createCell(3);
	        if(u.getMachine()!=null){
	        	cell.setCellValue(u.getMachine().getManageObjectName()); 
	        } 
	        cell.setCellStyle(style2);
	        
	        //被检部门
	        cell = row.createCell(4);
	        if(u.getCheckedDepartment()!=null){
	        	cell.setCellValue(u.getCheckedDepartment().getDepartmentName());  	
	        }
	        cell.setCellStyle(style2); 
	        
	        //问题描述
	        cell = row.createCell(5);
	        cell.setCellValue(u.getProblemDescription());  
	        cell.setCellStyle(style2);
	        
	        //扣分
	        cell = row.createCell(6);  
	        cell.setCellValue(u.getDeductPoints());  
	        cell.setCellStyle(style2); 
	        
	        //不符合项等级
	        cell = row.createCell(7);  
	        cell.setCellValue(u.getInconformityLevel()==null ?"":u.getInconformityLevel().toString());  
	        cell.setCellStyle(style2);
	        
	        //整改期限
	        cell = row.createCell(8);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCorrectDeadline()!=null){
	        	cell.setCellValue(u.getCorrectDeadline());  
	        }
	        cell.setCellStyle(style3);
	        
	        //整改建议
	        cell = row.createCell(9);
	        cell.setCellValue(u.getCorrectProposal());  
	        cell.setCellStyle(style2);
	        
	        //整改确认
	        cell = row.createCell(10);
	        cell.setCellValue((u.getHasCorrectConfirmed()!=null && u.getHasCorrectConfirmed()==true)?"是":"否");  
	        cell.setCellStyle(style2);
	        
	        //已复查
	        cell = row.createCell(11);
	        cell.setCellValue(u.getHasReviewed()!=null && u.getHasReviewed()==true ?"是":"否");  
	        cell.setCellStyle(style2);
	        
	        //整改完成
	        cell = row.createCell(12);
	        cell.setCellValue(u.getHasCorrectFinished()!=null && u.getHasCorrectFinished()==true?"是":"否");  
	        cell.setCellStyle(style2);
	        
	        //扣分指标
	        cell = row.createCell(13);
	        cell.setCellValue(u.getStandardIndex()!=null?u.getStandardIndex().getIndexName():"");  
	        cell.setCellStyle(style2);
	        
	        //危险源
	        cell = row.createCell(14);
	        cell.setCellValue(u.getHazrd()!=null ? u.getHazrd().getHazardDescription():"");  
	        cell.setCellStyle(style2);
	        
	        //扣分项
			String method="";
			int g=0;
			for(Entry<String,Integer> am:u.getAuditMethods().entrySet()){
				StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodDao.getBySn(am.getKey());
				if( standardIndexAuditMethod != null ){
					g++;
					//方法名
					method+=standardIndexAuditMethod.getAuditMethodContent()+",";
				}
			}
			if(g>0){
				method=method.substring(0, method.length()-1);
			}
	        cell = row.createCell(15);
	        cell.setCellValue(method);  
	        cell.setCellStyle(style2);
	        
	        //专业
	        cell = row.createCell(16);
	        cell.setCellValue(u.getSpeciality()!=null ? u.getSpeciality().getSpecialityName():"");
	        cell.setCellStyle(style2);
	        
	        //检查人
	        int i=0;
			String name="";
			for(Person person:u.getCheckers()){
				i++;
				name+=person.getPersonName()+",";
			}
			if(i>0){
				name=name.substring(0,name.length()-1);
			}
	        cell = row.createCell(17);
	        cell.setCellValue(name);  
	        cell.setCellStyle(style2);
	        
	        //整改负责人
	        cell = row.createCell(18);
	        cell.setCellValue(u.getCorrectPrincipal()!=null ? u.getCorrectPrincipal().getPersonName():"");  
	        cell.setCellStyle(style2);
	        
	        //录入人
	        cell = row.createCell(19);
	        cell.setCellValue(u.getEditor()!=null ? u.getEditor().getPersonName():"");  
	        cell.setCellStyle(style2);
		}
		try{
			ByteArrayOutputStream fout = new ByteArrayOutputStream();  
            wb.write(fout);
            wb.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            InputStream is = new ByteArrayInputStream(fileContent);
	    	return is;
		}catch(Exception e){
			return null;
		}
	}
	
}
