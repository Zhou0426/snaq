package cn.jagl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {
	/**
	 * �жϱ���Ƿ����Ӣ����ĸ��.-_�ȷ������
	 * @param str
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016.8.4
	 */
	public static Boolean isSn(String str){
		Pattern pattern = Pattern.compile("[-A-Za-z0-9._]*");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * �жϱ���Ƿ�����������
	 * @param str
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016.8.7
	 */
	public static Boolean isTwoNumber(String str){
		Pattern pattern = Pattern.compile("^\\d{2}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * �жϱ���Ƿ�����������
	 * @param str
	 * @return
	 * 
	 * @author ���ַ�
	 * @date 2016.8.7
	 */
	public static Boolean isNumber(String str){
		Pattern pattern = Pattern.compile("^\\d*$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
