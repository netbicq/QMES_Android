package kkkj.android.revgoods.mvpInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc：需要使用eventbus的activit和Fragment都需要以注解的方式绑定到此
 * author：xiedong
 * date：2017/10/17
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEventBus {
}

