package com.scaiz.zrpc.registry;

import com.scaiz.zrpc.Handler;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class Registry {

    private ConcurrentHashMap<Class, Object> registry = new ConcurrentHashMap<>();

    public void register(Class<?> clazz, Object bean) {
        if (!clazz.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException("Invalid registration");
        }
        registry.put(clazz, bean);
    }


    public Handler getHandler(String clazz, String method) {
        Class clz;
        try {
            clz = Class.forName(clazz);
        } catch (Exception e) {
            throw new RuntimeException("class not found for " + clazz);
        }
        if (!registry.containsKey(clz)) {
            throw new RuntimeException("No handler registered for class " + clazz);
        }
        Object bean = registry.get(clz);
        for (Method med : bean.getClass().getMethods()) {
            if (ReflectUtil.getMethodSignature(med).equals(method)) {
                return new Handler(bean, med);
            }
        }
        throw new RuntimeException(String.format("No method %s for class %s", method, clazz));
    }
}
