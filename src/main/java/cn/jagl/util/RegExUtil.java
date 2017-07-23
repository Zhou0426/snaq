package cn.jagl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {
	/**
	 * 判断编号是否仅由英文字母、.-_等符号组成
	 * @param str
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016.8.4
	 */
	public static Boolean isSn(String str){
		Pattern pattern = Pattern.compile("[-A-Za-z0-9._]*");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * 判断编号是否仅由数字组成
	 * @param str
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016.8.7
	 */
	public static Boolean isTwoNumber(String str){
		Pattern pattern = Pattern.compile("^\\d{2}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * 判断编号是否仅由数字组成
	 * @param str
	 * @return
	 * 
	 * @author 孟现飞
	 * @date 2016.8.7
	 */
	public static Boolean isNumber(String str){
		Pattern pattern = Pattern.compile("^\\d*$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
