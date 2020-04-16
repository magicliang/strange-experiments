package experiment.netty.example;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by magicliang on 2016/9/18.
 */
public class EchoServer extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // ctx 会自动帮我们处理release的问题
        ctx.write(msg);
        ctx.flush();
        //使用writeAndFlush？
    }
}
