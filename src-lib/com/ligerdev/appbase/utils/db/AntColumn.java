package com.ligerdev.appbase.utils.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AntColumn {

	public String name() default "";
	public String label() default "";
	public int size() default  -1;
	public boolean auto_increment() default false;
	
	public boolean tbl_visible() default true;
	public String tbl_icon() default "";
	public String tbl_format() default "";
}
