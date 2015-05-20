package com.ligerdev.appbase.utils.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AntTable {
	
	public static final String DATE_PATTERN = "$pattern$";
	
	public static enum DynamicAttr {
		$date_pattern$,
		$time_pattern$
	};
	public boolean show() default true;
	public String catalog() default "";
	public String name() default "";
	public String label() default "";
	public String key() default "";
	public String time_pattern() default "";
	public String sequence() default "";
}
