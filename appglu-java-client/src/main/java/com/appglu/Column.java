package com.appglu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface Column {

	String columnName() default "";
	
	boolean primaryKey() default false;
	
	boolean required() default false;
	
}