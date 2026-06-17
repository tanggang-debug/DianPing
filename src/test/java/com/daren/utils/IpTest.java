package com.daren.utils;

import java.net.InetAddress;

public class IpTest {
    public static void main(String[] args) throws Exception {
        // 获取本地 localhost 解析出的地址
        InetAddress address = InetAddress.getByName("localhost");
        System.out.println("当前 Java 客户端解析 localhost 的地址是: " + address.getHostAddress());
        // 检查是否强制开启了 IPv4 优先
        String preferIpv4 = System.getProperty("java.net.preferIPv4Stack");
        System.out.println("是否强制 IPv4 栈: " + (preferIpv4 != null ? preferIpv4 : "false"));
    }
}