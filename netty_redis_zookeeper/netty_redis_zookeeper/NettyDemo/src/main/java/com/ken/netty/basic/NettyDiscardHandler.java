package com.ken.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        ByteBuf in = (ByteBuf)msg;
        log.info("msg type:"+(in.hasArray()?"heap memory":"direct memory"));
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        log.info("server received:" + new String(arr,"UTF-8"));

        log.info("before write back,msg.refCnt:" + ((ByteBuf)msg).refCnt());

        ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener((ChannelFuture futureListener)->{
            log.info("after write back,msg.refCnt:" + ((ByteBuf)msg).refCnt());
        });
    }
}
