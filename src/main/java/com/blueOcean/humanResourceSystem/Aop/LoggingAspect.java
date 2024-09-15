package com.blueOcean.humanResourceSystem.Aop;

import com.blueOcean.humanResourceSystem.Annotation.LogMethod;
import com.blueOcean.humanResourceSystem.Utils.IPAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    // ThreadLocal to store start time for each thread
    private ThreadLocal<LocalDateTime> startTimeThreadLocal = new ThreadLocal<>();

    // Before advice: Executes before the annotated method
    @Before("@annotation(com.blueOcean.humanResourceSystem.Annotation.LogMethod)")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  // reflection
        Method method = signature.getMethod();
        LogMethod logMethod = method.getAnnotation(LogMethod.class);


        // Capture the start time and store it in ThreadLocal
        startTimeThreadLocal.set(LocalDateTime.now());

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        String developerNote = logMethod.value();
        String ipAddress = IPAddressUtil.getClientIpAddress(); // Use a utility class to get the IP address
        String threadName = Thread.currentThread().getName(); // Get the thread name

        log.info("{},START - Class: {}, Method: {},  IP: {}, Thread: {}",developerNote, className, methodName, ipAddress, threadName);
        log.info("Executing method: {} with arguments: {}", methodName, joinPoint.getArgs());
    }

    // AfterReturning advice: Executes after the method successfully returns
    @AfterReturning(pointcut = "@annotation(com.blueOcean.humanResourceSystem.Annotation.LogMethod)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        log.info("END - Class: {}, Method: {}, Return Value: {}", className, methodName, result);
        logExecutionTime(joinPoint);
    }

    // AfterThrowing advice: Executes if the method throws an exception
    @AfterThrowing(pointcut = "@annotation(com.blueOcean.humanResourceSystem.Annotation.LogMethod)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        log.error("EXCEPTION - Class: {}, Method: {}, Exception: {}", className, methodName, exception.getMessage());
        logExecutionTime(joinPoint);
    }

    // Utility method to log execution time
    private void logExecutionTime(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Assuming you store start time in some context (like a ThreadLocal or similar)
        // Retrieve the start time from ThreadLocal
        LocalDateTime startTime = startTimeThreadLocal.get();
        LocalDateTime endTime = LocalDateTime.now();
        long executionTime = ChronoUnit.MILLIS.between(startTime, endTime);

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        log.info("Execution time for Class: {}, Method: {} - Start Time: {}, End Time: {}, Execution Time: {} ms",
                className, methodName, startTime, endTime, executionTime);


        // Remove the start time from ThreadLocal to prevent memory leaks
        startTimeThreadLocal.remove();
    }
}
