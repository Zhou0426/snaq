package cn.jagl.util;

import org.junit.Test;

public class SmsUtilTest {

	@Test
	public void testsendSms() {
		int i=100;
		i=SmsUtil.sendSms(RandomUtil.getRandmDigital20(), "13775899277", java.time.LocalDateTime.now().toString());
		System.out.println(i);
	}

}
