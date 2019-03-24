package com.funnycode.hyjal.file.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tc
 * @date 2019-03-22
 */
public final class Reflection2Utils {

    public static Method findMethod(Class clazz, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            Method[] methods = clazz.getMethods();

            Optional<Method> first = Arrays.stream(methods).filter((Method x) ->
                x.getName().equals(methodName)).findFirst();

            method = first.orElseThrow(() ->
                new RuntimeException(methodName + "." + methodName + argumentTypesToString(parameterTypes)));
        }

        return method;
    }

    private static String argumentTypesToString(Class<?>[] argTypes) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                Class<?> c = argTypes[i];
                buf.append((c == null) ? "null" : c.getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }

}
