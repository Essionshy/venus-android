package com.tingyu.venus.netty;


import android.util.Log;

import com.tingyu.venus.listener.DispatcherWebSocketListener;
import com.tingyu.venus.model.Model;
import com.tingyu.venus.netty.protobuf.TransportMessageOuterClass;
import com.tingyu.venus.utils.Constants;
import com.tingyu.venus.utils.OkHttpClientUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import okhttp3.WebSocket;

/**
 * TCP Socket 客户端连接工具
 */
public class NettyClientConnectUtil {

   // public static final String DEFUALT_HOST = "192.168.0.105"; //windows
    public static final String DEFUALT_HOST = "192.168.0.103"; //linux
    public static final int DEFUALT_PORT = 11098;

    /**
     * 异步与服务器建立socket连接，并发送socket消息
     * @param handler
     */
    public static void connect(ChannelInboundHandlerAdapter handler) {
        Model.getInstance().getExecutor().execute(() -> {
            connect(DEFUALT_HOST, DEFUALT_PORT, handler);
        });
    }

    public static void connect(String host, int port, ChannelInboundHandlerAdapter handler) {
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("encoder", new ProtobufEncoder())
                                    .addLast("decoder", new ProtobufDecoder(TransportMessageOuterClass.TransportMessage.getDefaultInstance()))
                                    .addLast(handler);


                        }
                    });
            ChannelFuture cf = bootstrap.connect(host, port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            clientGroup.shutdownGracefully();
        }
    }

    /**
     * 建立WebSocket连接
     */
    public static WebSocket wsconnect() {
        WebSocket socket = null;
        FutureTask<WebSocket> futureTask = new FutureTask<>(new Callable<WebSocket>() {
            @Override
            public WebSocket call() throws Exception {
                WebSocket ws = OkHttpClientUtils.ws(Constants.WS_BASE_URL, new DispatcherWebSocketListener());
                Log.d("web", ws.toString());

                return ws;
            }
        });
        //执行
        Model.getInstance().getExecutor().execute(futureTask);

        while (!futureTask.isDone()) {

        }
        try {
            socket = futureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return socket;

    }
}
