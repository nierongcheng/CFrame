package com.codi.frame.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by Codi on 2015/4/23 0023.
 */
public class ReflectionUtil {

    /**
     * 返回是否调用成功
     *
     * @param className
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     */
    public static Object getMethod(String className, String methodName, Class<?>[] parameterTypes,
                                   Object[] args) throws Exception {
        Class<?> classes = Class.forName(className);
        Object instance = classes.newInstance();
        if (instance == null) {
            throw new Exception("-----------反射获取类实例:" + className + "失败，返回");
        }
        Method method = classes.getMethod(methodName, parameterTypes);
        Object roProductManufacturer = method.invoke(instance, args);
        return roProductManufacturer;
    }

    /**
     * 返回是否调用成功
     *
     * @param className
     * @param instance
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     */
    public static Object getMethod(String className, Object instance, String methodName,
                                   Class<?>[] parameterTypes, Object[] args) throws Exception {
        Class<?> classes = Class.forName(className);
        if (instance == null) {
            throw new Exception("-----------反射获取类实例:" + className + "失败，返回");
        }
        Method method = classes.getMethod(methodName, parameterTypes);
        Object roProductManufacturer = method.invoke(instance, args);
        return roProductManufacturer;
    }

    public static <T> T getClassInstance(String className, Class<?> parameterTypes, Object args) throws Exception {
        Class<?> classes = Class.forName(className);
        Constructor<?> constructor = classes.getConstructor(parameterTypes);
        T instance = (T) constructor.newInstance(args);
        return instance;
    }
}
