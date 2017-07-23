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
		//������ʱ�����session��
		if( !invocation.getProxy().getActionName().equals("sessionJudge") ){
			ServletActionContext.getRequest().getSession().setAttribute("sessionTime", LocalDateTime.now());
		}
		// ��ΪҪ��������������ó�Ĭ������������������ǵ�½��action��������
		if (PersonAction.class == invocation.getAction().getClass()
				&&(invocation.getProxy().getActionName().equals("login")
						||invocation.getProxy().getActionName().equals("modifypwd")
						||invocation.getProxy().getActionName().equals("sessionJudge"))) {
			return invocation.invoke();
		}
		//ȡsession�е�person���������null��û�е�½������login��ת����½ҳ��
		if (ServletActionContext.getRequest().getSession().getAttribute("personId") == null) {
			return "login";
		}else{
			if (springContext != null){
				PersonService personService=springContext.getBean("personService",PersonService.class);
				Person p=personService.getByPersonId(ServletActionContext.getRequest().getSession().getAttribute("personId").toString());
				if(p.getHasModifiedPwd()==null||!p.getHasModifiedPwd()){
					ActionSupport action = (ActionSupport) invocation.getAction();
					action.addActionMessage("�����޸ĳ�ʼ����󣬲��ܽ���ϵͳ��");
					return "mpwd";
				}
				if(!p.getSessionId().toString().equals(ServletActionContext.getRequest().getSession().getId().toString())){					
					ServletActionContext.getRequest().setAttribute("errorMessage", "�����ʺ���("+p.getIpAddress()+")�����µ�½�����������ߡ�����ⲻ�������˵Ĳ�����������������Ѿ�й¶�������������¼���޸����룡");
		            ServletActionContext.getRequest().getSession().invalidate();		            
					return Action.ERROR;//Ӧ�õ�����ҳ�棬����˺���������һ̨���Ե�½����ǿ�����ߣ���ע�����밲ȫ
				}
			}
		}
		//��¼���һ�β���ʱ��
		SessionInfoService sessionInfoService=springContext.getBean("sessionInfoService",SessionInfoService.class);
		SessionInfo sessionInfo=sessionInfoService.getByJsessionId(ServletActionContext.getRequest().getSession().getId());
		if(sessionInfo!=null){
			sessionInfo.setLastOperationDateTime(java.time.LocalDateTime.now());
			sessionInfoService.update(sessionInfo);
		}
		//дmenu�����ʵ���־�ļ�
		@SuppressWarnings("unchecked")
		HashMap<String,String> allMenuResources=(HashMap<String, String>) ServletActionContext.getServletContext().getAttribute("allMenuResources");
		if(allMenuResources.containsKey(actionUrl)){
			LoginInterceptor.log.info("personId:{},personName:{},resourceMenu:{}." , ServletActionContext.getRequest().getSession().getAttribute("personId"), ServletActionContext.getRequest().getSession().getAttribute("personName"),actionUrl);
		}
		result = invocation.invoke();
		return result;
	}
}
