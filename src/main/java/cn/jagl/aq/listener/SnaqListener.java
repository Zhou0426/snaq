package cn.jagl.aq.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.util.HashMap;

import javax.servlet.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.SessionInfo;
import cn.jagl.aq.service.ResourceService;
import cn.jagl.aq.service.SessionInfoService;

@WebListener
public class SnaqListener implements ServletContextListener,HttpSessionListener {
	private WebApplicationContext springContext;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		if (springContext != null) {
			//1.����ȫ������
//			FullTextSession fts = Search
//					.getFullTextSession(((SessionFactory) springContext.getBean("sessionFactory")).openSession());
//			try {
//				fts.createIndexer().startAndWait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			//2.��ȡ���е���Դ�嵥��Resource��������HashMap���Ա��������Ȩ����֤
			ResourceService resourceService=springContext.getBean("resourceService",ResourceService.class);
			HashMap<String,String> allMenuResources=new HashMap<String,String>();
			for(Resource resource:resourceService.getAllMenuResources()){
				allMenuResources.put(resource.getUrl(), resource.getResourceSn());
			}
			sce.getServletContext().setAttribute("allMenuResources", allMenuResources);
		} else {
			System.out.println("��ȡSpringӦ�ó���������ʧ��!");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}
}
