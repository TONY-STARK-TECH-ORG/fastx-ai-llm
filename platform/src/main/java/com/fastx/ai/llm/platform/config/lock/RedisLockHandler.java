package com.fastx.ai.llm.platform.config.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author stark
 */
@Slf4j
@Aspect
@Component
public class RedisLockHandler {

    final
    RedissonClient redissonClient;

    public RedisLockHandler(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String key = this.getRedisKey(joinPoint, redisLock);
        long leaseTime = redisLock.leaseTime();
        String errorDesc = redisLock.errorDesc();
        long waitTime = redisLock.waitTime();

        RLock rLock = redissonClient.getLock(key);
        boolean tryLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        if (!tryLock) {
            throw new RuntimeException(errorDesc);
        }
        try{
            return joinPoint.proceed();
        } catch (Exception ex) {
            throw ex;
        } finally{
            rLock.unlock();
        }
    }

    private String getRedisKey(ProceedingJoinPoint joinPoint, RedisLock redisLock) {
        String key = redisLock.key();
        Object[] parameterValues = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        if (StringUtils.isEmpty(key)) {
            if (parameterNames != null && parameterNames.length > 0) {
                StringBuilder sb = new StringBuilder();
                int i = 0;

                for(int len = parameterNames.length; i < len; ++i) {
                    sb.append(parameterNames[i]).append(" = ").append(parameterValues[i]);
                }

                key = sb.toString();
            } else {
                key = "redissonLock";
            }

            return key;
        } else {
            SpelExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(key);
            if (parameterNames != null && parameterNames.length != 0) {
                EvaluationContext evaluationContext = new StandardEvaluationContext();

                for(int i = 0; i < parameterNames.length; ++i) {
                    evaluationContext.setVariable(parameterNames[i], parameterValues[i]);
                }

                try {
                    Object expressionValue = expression.getValue(evaluationContext);
                    return expressionValue != null && !"".equals(expressionValue.toString()) ? expressionValue.toString() : key;
                } catch (Exception var13) {
                    return key;
                }
            } else {
                return key;
            }
        }
    }
}