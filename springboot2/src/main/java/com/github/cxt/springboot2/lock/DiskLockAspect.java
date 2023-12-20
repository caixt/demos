package com.github.cxt.springboot2.lock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class DiskLockAspect {
    @Autowired
    private LockManager lockManager;

    private ExpressionParser spel = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.github.cxt.springboot2.lock.DistLock)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object dealAround(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DistLock lockAnno = method.getAnnotation(DistLock.class);
        
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] argNames = discoverer.getParameterNames(method);
        Object[] argValues = pjp.getArgs();
        for (int i = 0; i < argNames.length; i++) {
            ctx.setVariable(argNames[i], argValues[i]);
        }
        String _class = method.getDeclaringClass().getName();
        String _method = method.getName();
        ctx.setVariable("_class", _class);
        ctx.setVariable("_method", _method);
        
        Object key = spel.parseExpression(lockAnno.value()).getValue(ctx);

        Lock lock = lockManager.getLock(String.valueOf(key));
        long waitMillis = lockAnno.waitMillis();

        boolean locked = waitMillis == -1 ? lock.lock() : lock.tryLock(lockAnno.waitMillis(), TimeUnit.MILLISECONDS);
        if (!locked) {
        	log.info("class:{},method:{},上锁失败,不执行", _class, _method);
            return null;
        }

        try {
            return pjp.proceed();
        } finally {
            lock.unlock();
        }
    }
}
