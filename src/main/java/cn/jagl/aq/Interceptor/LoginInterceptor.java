package cn.jagl.aq.Interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import cn.jagl.aq.action.PersonAction;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.SessionInfo;
import cn.jagl.aq.service.PersonService;
import cn.jagl.aq.service.SessionInfoService;

public class LoginInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
	private WebApplicationContext springContext;
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		springContext = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
		String result = "";
		HttpServletRequest request=ServletActionContext.getRequest();
		String path=request.getRequestURI();
		String contextPath=ServletActionContext.getServletContext().getContextPath();
		String actionUrl=path.replace(contextPath, "");
		//将请求时间放入session中
		if( !invocation.getProxy().getActionName().equals("sessionJudge") ){
			ServletActionContext.getRequest().getSession().setAttribute("sessionTime", LocalDateTime.now());
		}
		// 因为要把这个拦截器设置成默认拦截器，所以如果是登陆的action则跳过。
		if (PersonAction.class == invocation.getAction().getClass()
				&&(invocation.getProxy().getActionName().equals("login")
						||invocation.getProxy().getActionName().equals("modifypwd")
						||invocation.getProxy().getActionName().equals("sessionJudge"))) {
			return invocation.invoke();
		}
		//取session中的person，如果等于null则没有登陆，返回login跳转到登陆页面
		if (ServletActionContext.getRequest().getSession().getAttribute("personId") == null) {
			return "login";
		}else{
			if (springContext != null){
				PersonService personService=springContext.getBean("personService",PersonService.class);
				Person p=personService.getByPersonId(ServletActionContext.getRequest().getSession().getAttribute("personId").toString());
				if(p.getHasModifiedPwd()==null||!p.getHasModifiedPwd()){
					ActionSupport action = (ActionSupport) invocation.getAction();
					action.addActionMessage("必须修改初始密码后，才能进入系统！");
					return "mpwd";
				}
				if(!p.getSessionId().toString().equals(ServletActionContext.getRequest().getSession().getId().toString())){					
					ServletActionContext.getRequest().setAttribute("errorMessage", "您的帐号在("+p.getIpAddress()+")被重新登陆，您被迫下线。如果这不是您本人的操作，您的密码可能已经泄露。建议您尽快登录并修改密码！");
		            ServletActionContext.getRequest().getSession().invalidate();		            
					return Action.ERROR;//应该到提醒页面，你的账号已在另外一台电脑登陆，被强制下线，请注意密码安全
				}
			}
		}
		//记录最后一次操作时间
		SessionInfoService sessionInfoService=springContext.getBean("sessionInfoService",SessionInfoService.class);
		SessionInfo sessionInfo=sessionInfoService.getByJsessionId(ServletActionContext.getRequest().getSession().getId());
		if(sessionInfo!=null){
			sessionInfo.setLastOperationDateTime(java.time.LocalDateTime.now());
			sessionInfoService.update(sessionInfo);
		}
		//写menu被访问的日志文件
		@SuppressWarnings("unchecked")
		HashMap<String,String> allMenuResources=(HashMap<String, String>) ServletActionContext.getServletContext().getAttribute("allMenuResources");
		if(allMenuResources.containsKey(actionUrl)){
			LoginInterceptor.log.info("personId:{},personName:{},resourceMenu:{}." , ServletActionContext.getRequest().getSession().getAttribute("personId"), ServletActionContext.getRequest().getSession().getAttribute("personName"),actionUrl);
		}
		result = invocation.invoke();
		return result;
	}
}
