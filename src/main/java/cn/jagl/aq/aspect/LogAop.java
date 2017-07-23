package cn.jagl.aq.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAop {
	@Pointcut("execution(* cn.jagl.aq.service.*.*(..))")
	private void pointCutMethod() {
	}

	//声明前置通知
//	@Before("pointCutMethod()")
//	public void doBefore(JoinPoint jp) {
//		System.out.println(
//				"log Begining method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
//	}

	//声明环绕通知
//	@Around("pointCutMethod()")
//	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//		long time = System.currentTimeMillis();
//		Object retVal = pjp.proceed();
//		time = System.currentTimeMillis() - time;
//		System.out.println("process time: " + time + " ms");
//		return retVal;
//	}

	//声明后置通知
//	@AfterReturning(pointcut = "pointCutMethod()", returning = "result")
//	public void doAfter(JoinPoint jp) {
//		System.out.println(
//				"log Ending method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
//	}

	//声明例外通知
	@AfterThrowing(pointcut = "pointCutMethod()", throwing = "e")
	public void doThrowing(JoinPoint jp, Throwable e) {
		System.out.println("method " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName()
				+ " throw exception");
		System.out.println(e.getMessage());
	}
}
