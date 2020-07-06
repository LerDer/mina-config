package com.lww.mina.protocol;

import java.nio.charset.Charset;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 自定义编码器工厂
 *
 * @author lww
 * @date 2020-07-06 22:26
 */
public class MessageProtocolCodecFactory implements ProtocolCodecFactory {

    private final ProtocolEncoder encoder;
    private final ProtocolDecoder decoder;

    public MessageProtocolCodecFactory() {
        this(Charset.forName("UTF-8"));
    }

    public MessageProtocolCodecFactory(Charset charset) {
        this.encoder = new MessageProtocolEncoder(charset);
        this.decoder = new MessageProtocolDecoder(charset);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) {
        return decoder;
    }
}
