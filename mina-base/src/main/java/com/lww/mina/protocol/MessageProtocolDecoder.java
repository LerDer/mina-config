package com.lww.mina.protocol;

import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 自定义解码器
 *
 * @author lww
 * @date 2020-07-06 22:33
 */
@Slf4j
public class MessageProtocolDecoder extends CumulativeProtocolDecoder {

    private final Charset charset;

    public MessageProtocolDecoder() {
        this.charset = Charset.defaultCharset();
    }

    /**
     * 构造方法注入编码格式
     */
    public MessageProtocolDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        // 包头的长度
        // 拆包时，如果可读数据的长度小于包头的长度，就不进行读取
        if (in.remaining() < MessagePack.PACK_HEAD_LEN) {
            return false;
        } else {
            //标记当前position，以便后继的reset操作能恢复position位置
            in.mark();
            // 获取总长度
            int length = in.getInt();
            //获取模块代码
            int module = in.getInt();
            log.info("CustomProtocolDecoder_doDecode_length:{}, module:{}", length, module);
            // 如果可读取数据的长度 小于 总长度 - 包头的长度 ，则结束拆包，等待下一次
            if (in.remaining() < (length - MessagePack.PACK_HEAD_LEN)) {
                in.reset();
                return false;
            } else {
                //重置回复position位置到操作前  并读取一条完整记录
                in.reset();
                byte[] bytes = new byte[length];
                // 获取长度4个字节、模块4个字节、内容，即获取完整消息
                in.get(bytes, 0, length);
                String content = new String(bytes, MessagePack.PACK_HEAD_LEN, length - MessagePack.PACK_HEAD_LEN, charset);
                // 封装为自定义的java对象
                MessagePack pack = new MessagePack(module, content);
                out.write(pack);
                // 如果读取一条记录后，还存在数据（粘包），则再次进行调用
                return in.remaining() > 0;
            }
        }
    }
}