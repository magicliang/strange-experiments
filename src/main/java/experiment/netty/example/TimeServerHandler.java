package experiment.netty.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * Created by magicliang on 2016/9/18.
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        final ChannelFuture f;
        f = ctx.writeAndFlush(time);
        // 这算不算是 lambda 的 type witness
        f.addListener((ChannelFutureListener) future -> {
            //因为在Netty里所有的操作都是异步的。所以要在这个future完成以后再关闭
            assert f == future;//为什么在这里要断言
            ctx.close();
        });
        //f.addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
