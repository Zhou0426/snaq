package cn.jagl.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
public class SmsUtil {
	private static String url;
	private static String spCode;
	private static String loginName;
	private static String password;
	//加载配置数据的静态代码块
	static{
		Resource resource = new ClassPathResource("smsconf.properties");
		InputStream in;
		try {
			in = resource.getInputStream();
			Properties prop = new Properties();
			prop.load(in);
			url=prop.getProperty("url");
			spCode=prop.getProperty("spCode");
			loginName=prop.getProperty("loginName");
			password=prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 
	 * @param serialNumber 流水号，可以调用RandomUtil.getRandmDigital20()
	 * @param userNumber 手机号
	 * @param messageContent 短信内容
	 * @return 返回0表示发送成功，非0表示失败
	 */
	public static int sendSms(String serialNumber,String userNumber,String messageContent){
		String info = null;
		try{
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);//
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
			post.addParameter("SpCode", spCode);
			post.addParameter("LoginName", loginName);
			post.addParameter("Password", password);
			post.addParameter("MessageContent", "风险预控系统{"+messageContent+"}");
			post.addParameter("UserNumber", userNumber);
			post.addParameter("SerialNumber", serialNumber);
			post.addParameter("ScheduleTime", "");
			post.addParameter("ExtendAccessNum", "");
			post.addParameter("f", "1");
			httpclient.executeMethod(post);
			info = new String(post.getResponseBody(),"gbk");
			return Integer.valueOf(info.substring(info.indexOf("result=")+7, info.indexOf("&description=")));			
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
