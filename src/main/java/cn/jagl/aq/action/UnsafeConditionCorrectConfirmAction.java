package cn.jagl.aq.action;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Sms;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.domain.UnsafeConditionCorrectConfirm;
import cn.jagl.util.RandomUtil;
import cn.jagl.util.SmsUtil;

/**
 * @author mahui
 * @method 
 * @date 2016��7��23������4:40:25
 */
public class UnsafeConditionCorrectConfirmAction extends BaseAction<UnsafeConditionCorrectConfirm> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String confirmInfo;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getConfirmInfo() {
		return confirmInfo;
	}
	public void setConfirmInfo(String confirmInfo) {
		this.confirmInfo = confirmInfo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//��ѯ
	public String query(){
		jsonList=unsafeConditionCorrectConfirmService.query(id);
		return "jsonList";
	}
	//���
	public String save(){
		boolean isSuccess=true;
		message="ok";
		Date date = new Date();//���ϵͳʱ��.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//��ʱ���ʽת���ɷ���TimestampҪ��ĸ�ʽ.
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//��ʱ��ת��
		UnsafeCondition unsafeCondition=unsafeConditionService.getById(id);
		UnsafeConditionCorrectConfirm unsafeConditionCorrectConfirm=new UnsafeConditionCorrectConfirm();
		unsafeConditionCorrectConfirm.setConfirmer(unsafeCondition.getCorrectPrincipal());
		unsafeConditionCorrectConfirm.setConfirmInfo(confirmInfo);
		unsafeConditionCorrectConfirm.setConfirmTime(goodsC_date);
		unsafeConditionCorrectConfirm.setInconformityItem(unsafeCondition);
		unsafeCondition.setConfirmTime(LocalDate.now());
		unsafeCondition.setHasCorrectConfirmed(true);
		unsafeCondition.setCurrentRiskLevel(null);
		try{						
			unsafeConditionCorrectConfirmService.save(unsafeConditionCorrectConfirm);
			unsafeConditionService.update(unsafeCondition);
		}catch(Exception e){
			message="nook";
			isSuccess=false;
		}
		if(isSuccess=true&&unsafeCondition!=null){
			LocalDateTime now=LocalDateTime.now();
			Set<Person> personSet = new HashSet<Person>(); 
			personSet.addAll(unsafeCondition.getCheckers());
			personSet.add(unsafeCondition.getEditor());
			for(Person person:personSet){
				if(person.getCellphoneNumber()!=null&&person.getCellphoneNumber().trim().length()==11){
					String serialNumber=RandomUtil.getRandmDigital20();
					String userNumber=person.getCellphoneNumber();
					String messageContent=person.getPersonName()+"�����µ������Ѿ�����ȷ�ϣ���Ҫ���飬����ȷ��ʱ�䣺"+now.getYear()+"��"+now.getMonthValue()+"��"+now.getDayOfMonth()+"��"+now.getHour()+"ʱ"+now.getMinute()+"��";
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
			}
			
		}
		return SUCCESS;
	}
	
}
