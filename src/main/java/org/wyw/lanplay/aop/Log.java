package org.wyw.lanplay.aop;

import java.lang.annotation.*;

/**
 * @author wuyd
 * 创建时间：2019/10/23 15:37
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    String desc()  default "";
}
