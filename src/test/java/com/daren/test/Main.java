package com.daren.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Service {
    void perform();
}

class ServiceImpl implements Service {
    public void perform() {
        System.out.println("Service performed.");
    }
}

class ServiceInvocationHandler implements InvocationHandler {
    private Object target;

    public ServiceInvocationHandler(Object target) {
        this.target = target;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before service");
        Object result = method.invoke(target, args);
        System.out.println("After service");
        return result;
    }
}

 class JDKProxyDemo {
    public static void main(String[] args) {
        Service service = new ServiceImpl();
        Service proxy = (Service) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new ServiceInvocationHandler(service)
        );
        proxy.perform();
    }
}