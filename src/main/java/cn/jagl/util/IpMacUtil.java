package cn.jagl.util;

import javax.servlet.http.HttpServletRequest;
/**
 * 获取客户端的真实ip地址（不完全准确）
 * @author 孟现飞
 * @date 2016-8-18
 */
public class IpMacUtil {
	public static String getRealIPAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
	    if(null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("X-Real-IP");
	    }
	    if(null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	}
}
