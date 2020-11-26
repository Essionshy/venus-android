package com.tingyu.venus.netty.util;

import java.util.Random;

/**
 * @Author essionshy
 * @Create 2020/11/21 13:45
 * @Version venus-server
 */
public class MessageIdGenerator {

    private static Long id=null;

    private MessageIdGenerator(){}


    public static Long getId(){
        if(id==null){
            id=Math.abs(new Random().nextLong());
        }else{
            id++;
        }

        return id;
    }

}
