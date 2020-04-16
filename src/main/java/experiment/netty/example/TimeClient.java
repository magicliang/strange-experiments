package experiment.netty.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by magicliang on 2016/9/18.
 */
// 这里没有用bind，但可以用bind
public class TimeClient {
    public static void main(String[] args) throws Exception {
        int port;
        String host;

        if (args.length > 1) {
            port = Integer.parseInt(args[0]);
            host = args[0];
        } else {
            port = 8080;
            host = "localhost";
        }
        // 不管是 client 还是 server，使用的 EventLoopGroup线程组始终是不变的
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // client 并没有使用 ServerBootstrap，所以也只需要一个线程组
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);//客户端也使用niochannel
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                }

            });
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
