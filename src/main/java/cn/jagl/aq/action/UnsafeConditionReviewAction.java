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
 * @date 2016��7��23������4:41:45
 */
public class UnsafeConditionReviewAction extends BaseAction<UnsafeConditionReview> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String message;
	private Timestamp reviewTime;//����ʱ��
	private String reviewInfo;//������Ϣ
	private Boolean hasCorrectFinished;//���������
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
	//��ѯ
	public String query(){
		jsonList=unsafeConditionReviewService.query(id);
		return "jsonList";
	}
	//���
	public String save(){
		boolean isSuccess=true;
		message="ok";
		String str="";
		Date date = new Date();//���ϵͳʱ��.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//��ʱ���ʽת���ɷ���TimestampҪ��ĸ�ʽ.
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//��ʱ��ת��
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
        //�ж��Ƿ�����ͨ��
        if(hasCorrectFinished==true){
        	unsafeCondition.setHasReviewed(true);
        	unsafeCondition.setHasCorrectFinished(true);
        	str="����һ�������Ѿ�����ͨ����";
        }else{
        	unsafeCondition.setConfirmTime(null);
        	unsafeCondition.setHasCorrectConfirmed(false);
        	unsafeCondition.setHasReviewed(false);
        	str="����һ����������û��ͨ�����뾡��ǰ��ϵͳ�鿴��";
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
         	String messageContent=unsafeCondition.getCheckerFrom()+"��飺"+str+"������Ϣ�ύʱ��:"+now.getYear()+"��"+now.getMonthValue()+"��"+now.getDayOfMonth()+"��"+now.getHour()+"ʱ"+now.getMinute()+"��";
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
