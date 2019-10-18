package com.scaiz.zrpc.service;

public interface HelloService {
     String hello(String xx);

     public static void main(String[] args) {
         System.out.println(HelloService.class.getMethods()[0]);
     }
}
