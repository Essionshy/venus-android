package com.tingyu.venus.netty;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 与服务器端建立通信
 */
public class NettyClient {


    /**
     * 启动默认HOST与PORT的TCP连接
     * @param handler
     */
    public void run(ChannelInboundHandlerAdapter handler){
        run(NettyClientConnectUtil.DEFUALT_HOST,NettyClientConnectUtil.DEFUALT_PORT,handler);
    }

    public void run(String host, int port, ChannelInboundHandlerAdapter handler){
        NettyClientConnectUtil.connect(host,port,handler);
    }

}
