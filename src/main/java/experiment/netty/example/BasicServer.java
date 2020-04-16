package experiment.netty.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by magicliang on 2016/9/18.
 */
/*
 * 1 先制造BootStrap
 * 2 再选定一个boss，一个worker两个线程组
 * 3 对这个channel配置handler，所有的参数和处理逻辑都在handler里了
 * 4 阻塞本线程直到channel关闭
 * 5 关闭线程池
 * 6 退出本线程
 *
 * */
public class BasicServer {
    private int port;

    public BasicServer() {
        super();
    }

    public BasicServer(int port) {
        this();
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new BasicServer(port).run();
    }

    public void run() throws Exception {
        // 事件循环组就是线程池的组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            // 注意，这里引入了一个parenGroup和childgroup的概念，不是不定参数
            // 这底层应该是两个管理 NioEvent的线程组
            b.group(bossGroup // 主线程组来进行accept操作
                    , workerGroup)// 子线程组来管理读写
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 在channel的管道中加入一段修饰逻辑
                            // 也就是说在所有的连接里，所有的channel都被这段逻辑增强了
                            // ch.pipeline().addLast(new EchoServer());
                            // 只有最先的 addLast 调用里的handler能起作用
                            //ch.pipeline().addLast(new DiscardServerHandler());
                            ch.pipeline().addLast(new TimeServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();//绑定的服务器;sync 等待服务器关闭。也就是说sync到关闭之前都是阻塞的？
            f.channel().closeFuture().sync();//阻塞完了到这一步，channel才能关？
        } finally {
            //这是两个线程池的重要证据
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
