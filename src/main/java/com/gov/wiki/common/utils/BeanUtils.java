package com.gov.wiki.common.utils;

import static com.gov.wiki.common.utils.ClassUtils.getRealType;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.exception.BeanChangException;
import com.gov.wiki.common.res.PageResult;
import org.springframework.data.domain.Page;


import lombok.extern.slf4j.Slf4j;

/**
 * Java类相关操作
 * @author sdfasfgh
 *
 */
@Slf4j
public class BeanUtils {

    private final static Map<Class<?>, CachePropertyDescriptor> softCache = new ConcurrentHashMap<Class<?>, CachePropertyDescriptor>();

    public static <T> List<T> listCopy(Collection<?> source,Class<T> clazz){
        List<T> list = new ArrayList<T>();
        copysProperties(source, list, clazz);
        return list;
    }
    public static <T> Set<T> setCopy(Collection<?> source,Class<T> clazz){
        Set<T> list = new LinkedHashSet<T>();
        copysProperties(source, list, clazz);
        return list;
    }

    public static <T> T copyProperties(Object source,Class<T> targetClass) {
        T t = (T) copyProperties(source, targetClass, true);
        return t;
    }

    /**
     * 集合转换
     * @param source
     * @param targetClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static void copysProperties(Collection source,Collection target,Class<?> clazz) {
        for (Object object : source) {
            target.add(copyProperties(object, clazz, true));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T copyProperties(Object source,Class<T> targetClass,boolean ChangsSon) {
        T t = (T) doCopyProperties(source, targetClass, ChangsSon);
        return t;
    }




    public static Object doCopyProperties(Object source, Class targetClass,boolean ChangsSon, String... ignoreProperties) {
        try {
            Object tagObject = targetClass.newInstance();
            if(source == null) return null;
            copyProperties(source, tagObject, ChangsSon, ignoreProperties);
            return tagObject;
        } catch (Exception e) {
            throw new BeanChangException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void copyProperties(Object source, Object target,boolean changsSon, String... ignoreProperties) {
        if(source == null) {
            throw new BeanChangException("Source must not be null");
        }
        if(target == null) {
            throw new BeanChangException("Target must not be null");
        }

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds;
        try {
            targetPds = getPropertyDescriptors(actualEditable);
        } catch (IntrospectionException e) {
            throw new BeanChangException(e);
        }
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd;
                try {
                    sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                } catch (IntrospectionException e) {
                    throw new BeanChangException(e);
                }
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if(value == null) continue;
                            if(value instanceof List) {
                                if(Set.class.isAssignableFrom(writeMethod.getParameterTypes()[0])) {
                                    value = setCopy((Collection<?>) value, getRealType(getField(actualEditable,targetPd.getName())));
                                } else {
                                    value = listCopy((Collection<?>) value, getRealType(getField(actualEditable,targetPd.getName())));
                                }
                            } else if(value instanceof Set) {
                                if(Set.class.isAssignableFrom(writeMethod.getParameterTypes()[0])) {
                                    value = setCopy((Collection<?>) value, getRealType(getField(actualEditable,targetPd.getName())));
                                } else {
                                    value = listCopy((Collection<?>) value, getRealType(getField(actualEditable,targetPd.getName())));
                                }
                            }
                            if(!ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], value.getClass())) {
                                if(changsSon) value = copyProperties(value, writeMethod.getParameterTypes()[0], changsSon);
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new BeanChangException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    private static Field getField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        return clazz.getDeclaredField(name);
    }


    public static void main(String[] args) throws NoSuchFieldException, SecurityException {
        Field f = PrivResource.class.getDeclaredField("childList");
        System.out.println(getRealType(f));
    }

    private static PropertyDescriptor getPropertyDescriptor(Class<? extends Object> clazz, String name) throws IntrospectionException {
        CachePropertyDescriptor cr = cachePropertyDescriptorforClass(clazz);
        return cr.getPropertyDescriptor(name);
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
        CachePropertyDescriptor cr = cachePropertyDescriptorforClass(clazz);
        return cr.getPropertyDescriptors();
    }

    private static CachePropertyDescriptor cachePropertyDescriptorforClass(Class<?> clazz) throws IntrospectionException {
        CachePropertyDescriptor cache = softCache.get(clazz);
        if(cache == null) {
            cache = CachePropertyDescriptor.createForClass(clazz);
            softCache.put(clazz, cache);
        }
        return cache;
    }

    public static <T> PageResult<T> pageCopy(Page<?> oldPage, Class<T> clazz) {
        PageResult<T> newPage = new PageResult<T>();
        newPage.setCurrentPage(oldPage.getNumber());
        newPage.setPageSize(oldPage.getSize());
        newPage.setTotal(oldPage.getTotalElements());
        newPage.setTotalPages(oldPage.getTotalPages());
        List<?> oldList = oldPage.getContent();
        List<T> newList = listCopy(oldList, clazz);
        newPage.setDataList(newList);
        return newPage;
    }


}

class CachePropertyDescriptor{
    private final Map<String, PropertyDescriptor> cache = new ConcurrentHashMap<String, PropertyDescriptor>();

    static CachePropertyDescriptor createForClass(Class<?> clazz) throws IntrospectionException {
        CachePropertyDescriptor bean = new CachePropertyDescriptor();
        PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            bean.cache.put(pd.getName(), pd);
        }
        return bean;
    }

    public PropertyDescriptor getPropertyDescriptor(String name) {
        for (PropertyDescriptor pd : cache.values()) {
            if(pd.getName().equals(name)) return pd;
        }
        return null;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        int i = 0;
        PropertyDescriptor[] pds = new PropertyDescriptor[cache.size()];
        for (PropertyDescriptor propertyDescriptor : cache.values()) {
            pds[i] = propertyDescriptor;
            i++;
        }
        return pds;
    }
}

