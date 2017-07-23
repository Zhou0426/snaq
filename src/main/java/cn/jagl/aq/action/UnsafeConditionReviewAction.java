package cn.jagl.aq.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Sms;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.domain.UnsafeConditionReview;
import cn.jagl.util.RandomUtil;
import cn.jagl.util.SmsUtil;

/**
 * @author mahui
 * @method 
 * @date 2016年7月23日下午4:41:45
 */
public class UnsafeConditionReviewAction extends BaseAction<UnsafeConditionReview> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String message;
	private Timestamp reviewTime;//复查时间
	private String reviewInfo;//复查信息
	private Boolean hasCorrectFinished;//已整改完成
	private String personId;
	
	

	public String getMessage() {
		return message;
	}
	public String getReviewInfo() {
		return reviewInfo;
	}
	public void setReviewInfo(String reviewInfo) {
		this.reviewInfo = reviewInfo;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Timestamp reviewTime) {
		this.reviewTime = reviewTime;
	}
	public Boolean getHasCorrectFinished() {
		return hasCorrectFinished;
	}
	public void setHasCorrectFinished(Boolean hasCorrectFinished) {
		this.hasCorrectFinished = hasCorrectFinished;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//查询
	public String query(){
		jsonList=unsafeConditionReviewService.query(id);
		return "jsonList";
	}
	//添加
	public String save(){
		boolean isSuccess=true;
		message="ok";
		String str="";
		Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
        UnsafeCondition unsafeCondition=unsafeConditionService.getById(id);
        UnsafeConditionReview unsafeConditionReview=new UnsafeConditionReview();
        unsafeConditionReview.setHasCorrectFinished(hasCorrectFinished);
        unsafeConditionReview.setReviewInfo(reviewInfo);
        unsafeConditionReview.setInputTime(goodsC_date);
        unsafeConditionReview.setReviewTime(reviewTime);
        Person p=personService.getByPersonId(personId);
        if(p!=null){
        	unsafeConditionReview.setReviewer(p);
        }else{
        	message="nook";
        	return SUCCESS;
        }
        
        unsafeConditionReview.setInconformityItem(unsafeCondition);
        //判断是否整改通过
        if(hasCorrectFinished==true){
        	unsafeCondition.setHasReviewed(true);
        	unsafeCondition.setHasCorrectFinished(true);
        	str="您有一条隐患已经复查通过！";
        }else{
        	unsafeCondition.setConfirmTime(null);
        	unsafeCondition.setHasCorrectConfirmed(false);
        	unsafeCondition.setHasReviewed(false);
        	str="您有一条隐患复查没有通过！请尽快前往系统查看！";
        }
        try{
        	unsafeConditionReviewService.save(unsafeConditionReview);
        	unsafeConditionService.update(unsafeCondition);
        }catch(Exception e){
			message="nook";
			isSuccess=false;
		}
        Person person=unsafeCondition.getCorrectPrincipal();
        if(isSuccess=true&&person.getCellphoneNumber()!=null&&person.getCellphoneNumber().trim().length()==11){
        	LocalDateTime now=LocalDateTime.now();
         	String serialNumber=RandomUtil.getRandmDigital20();
         	String userNumber=person.getCellphoneNumber();
         	String messageContent=unsafeCondition.getCheckerFrom()+"检查："+str+"复查信息提交时间:"+now.getYear()+"年"+now.getMonthValue()+"月"+now.getDayOfMonth()+"日"+now.getHour()+"时"+now.getMinute()+"分";
     		Sms sms=new Sms();
     		sms.setMessageContent(messageContent);
     		sms.setResultCode(SmsUtil.sendSms(serialNumber, userNumber, messageContent));
     		sms.setSerialNumber(serialNumber);
     		sms.setUserNumber(userNumber);
     		sms.setSuccessTimestamp(Timestamp.valueOf(now));
     		try{
     			smsService.save(sms);
     		}catch(Exception e){
     			System.out.println(e);
     		}
        }
        return SUCCESS;
	}
	
}
