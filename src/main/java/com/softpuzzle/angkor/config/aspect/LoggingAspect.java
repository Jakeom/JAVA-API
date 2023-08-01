package com.softpuzzle.angkor.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.softpuzzle.angkor.aspect.Trace.LogTrace;
import com.softpuzzle.angkor.aspect.Trace.LogTraceAbstractTemplate;

import lombok.RequiredArgsConstructor;

/**
 * Aspect for logging execution of service and repository Spring components.
 * 
 * @author Ramesh Fadatare
 *
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final LogTrace trace;

	/**
	 * Pointcut that matches all repositories, services and Web REST endpoints.
	 */
	@Pointcut("within(@org.springframework.stereotype.Repository *)" + " || within(@org.springframework.stereotype.Service *)" + " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	/**
	 * Pointcut that matches all Spring beans in the application's main packages.
	 */
	@Pointcut("within(com.softpuzzle.angkor.mapper.*)" + " || within(com.softpuzzle.angkor.service..*)" + " || within(com.softpuzzle.angkor.controller..*)")
	public void applicationPackagePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	/**
	 * Advice that logs methods throwing exceptions.
	 *
	 * @param joinPoint join point for advice
	 * @param e         exception
	 */
	@AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	/**
	 * Advice that logs when a method is entered and exited.
	 *
	 * @param joinPoint join point for advice
	 * @return result
	 * @throws Throwable throws IllegalArgumentException
	 */
	@Around("applicationPackagePointcut() && springBeanPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		
		LogTraceAbstractTemplate<String> template = new LogTraceAbstractTemplate<String>(trace) {
			@Override
			protected Object call() {
				Object result=null;
				try {
					result = joinPoint.proceed();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return result;
			}
		};
		
		Object result =(Object) template.execute(joinPoint.getSignature().getDeclaringTypeName()+" : "+joinPoint.getSignature().getName());
			
		return result;
		
	}
}
