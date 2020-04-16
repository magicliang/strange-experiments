package experiment.netty.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by magicliang on 2016/9/18.
 */
public class TimeDecoder extends ByteToMessageDecoder {
    // 有了这个Decoder，才可以彻底解决底层字节分片组装的问题，只有字节够了才返回
    // 更好的做法应该是使用 POJO，想办法把字节映射到对象而不是基本类型
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return; // (3)
        }
        out.add(in.readBytes(4)); // (4)
    }
}
