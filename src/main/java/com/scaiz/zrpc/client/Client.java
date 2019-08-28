package com.scaiz.zrpc.client;

import com.scaiz.zrpc.netty.RpcClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) {
        RpcClient rpcClient = RpcClient.getInstance();
        rpcClient.init();

        Executors.newSingleThreadExecutor().submit(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String cmd;
                try {
                    cmd = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                if ("exit".equals(cmd)) {
                    break;
                }

                try {
                    Object rsp = rpcClient.sendMsgWithResponse(cmd);
                    System.out.println(rsp);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                reader.close();
            } catch (Exception ignore) {
            }
        });
    }
}
