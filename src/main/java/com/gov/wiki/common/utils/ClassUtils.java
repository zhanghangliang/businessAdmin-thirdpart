package com.gov.wiki.common.utils;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ClassUtils {

    public static Field[] getAllField(Class clazz) {
        if(clazz.equals(Object.class)) return new Field[0];
        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = getAllField(clazz.getSuperclass());
        if(superFields.length == 0) return fields;
        fields = Arrays.copyOf(fields, fields.length+superFields.length);
        System.arraycopy(superFields, 0, fields, fields.length-superFields.length, superFields.length);
        return fields;
    }

    public static Class getRealType(Field clazz){
        // 获取当前new的对象的泛型的父类类型
        ParameterizedType pt = (ParameterizedType) clazz.getGenericType();
        // 获取第一个类型参数的真实类型
        Type type =  pt.getActualTypeArguments()[0];
        return (Class) type;
    }

    public static String methodGetName(String columnName) {
        Assert.hasText(columnName, "columnName must not Be is Null");
        columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
        return "get"+columnName;
    }
    public static String methodSetName(String columnName) {
        Assert.hasText(columnName, "columnName must not Be is Null");
        columnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
        return "set"+columnName;
    }

    public static boolean isAssignable(Class<?> class1, Class<?> returnType) {
        return org.springframework.util.ClassUtils.isAssignable(class1, returnType);
    }

    public static Method[] getAllMethod(Class clazz) {
        if(clazz.equals(Object.class)) return new Method[0];
        Method[] methods = clazz.getDeclaredMethods();
        Method[] superMethods = getAllMethod(clazz.getSuperclass());
        if(superMethods.length == 0) return methods;
        methods = Arrays.copyOf(methods, methods.length+superMethods.length);
        System.arraycopy(superMethods, 0, methods, methods.length-superMethods.length, superMethods.length);
        return methods;
    }
}
