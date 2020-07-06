package com.lww.mina.protocol;

import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * java对象转二进制 编码
 *
 * @author lww
 * @date 2020-07-06 22:29
 */
@Slf4j
public class MessageProtocolEncoder extends ProtocolEncoderAdapter {

    private final Charset charset;

    public MessageProtocolEncoder() {
        this(Charset.defaultCharset());
    }

    public MessageProtocolEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        MessagePack pack = (MessagePack) message;
        //设置缓冲区大小，并自动增长
        IoBuffer ioBuffer = IoBuffer.allocate(pack.getLen()).setAutoExpand(true);
        log.info("MessageProtocolEncoder_encode_length:{}", pack.getLen());
        //放置长度
        ioBuffer.putInt(pack.getLen());
        //放置模块代码
        ioBuffer.putInt(pack.getModule());
        if (StringUtils.isNotBlank(pack.getBody())) {
            log.info("MessageProtocolEncoder_encode_length:{}", pack.getBody().getBytes().length);
            //放置字节数组
            ioBuffer.putString(pack.getBody(), charset.newEncoder());
        }
        ioBuffer.flip();
        out.write(ioBuffer);
    }
}
