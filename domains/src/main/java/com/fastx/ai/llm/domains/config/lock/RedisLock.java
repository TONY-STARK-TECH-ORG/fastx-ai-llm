package com.fastx.ai.llm.domains.config.lock;

import java.lang.annotation.*;

/**
 * @author stark
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisLock {

    String key();

    long waitTime() default 10;

    long leaseTime() default 10;

    boolean autoRelease() default true;

    String errorDesc() default "压力好大，请你等个几秒再试一次吧";
}
