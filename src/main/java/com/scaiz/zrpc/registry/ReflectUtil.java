package com.scaiz.zrpc.registry;

import java.lang.reflect.Method;

public class ReflectUtil {

    public static String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append("(");
        for (Class<?> clz : method.getParameterTypes()) {
            sb.append(clz.getName());
        }
        sb.append(")");
        return sb.toString();
    }
}
