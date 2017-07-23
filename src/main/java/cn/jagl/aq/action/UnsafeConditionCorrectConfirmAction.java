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
 * @date 2016年7月23日下午4:40:25
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
	//查询
	public String query(){
		jsonList=unsafeConditionCorrectConfirmService.query(id);
		return "jsonList";
	}
	//添加
	public String save(){
		boolean isSuccess=true;
		message="ok";
		Date date = new Date();//获得系统时间.
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换
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
					String messageContent=person.getPersonName()+"：有新的隐患已经整改确认！需要复查，整改确认时间："+now.getYear()+"年"+now.getMonthValue()+"月"+now.getDayOfMonth()+"日"+now.getHour()+"时"+now.getMinute()+"分";
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
