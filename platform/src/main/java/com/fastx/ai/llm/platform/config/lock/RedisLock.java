package com.fastx.ai.llm.platform.config.lock;

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

    String errorDesc() default "you are too fast to visit! please try again later.";
}
