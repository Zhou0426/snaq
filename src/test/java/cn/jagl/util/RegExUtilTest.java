package cn.jagl.util;

import javax.annotation.Resource;

import org.junit.Test;


public class RegExUtilTest {
	@Resource RegExUtil regExUtil;  
	
	@SuppressWarnings("static-access")
	@Test
	public void testIsNumber() {

		
		System.out.println(regExUtil.isNumber("23"));
	}
}
