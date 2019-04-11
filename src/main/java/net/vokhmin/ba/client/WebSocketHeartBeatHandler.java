package net.vokhmin.ba.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class WebSocketHeartBeatHandler extends ChannelDuplexHandler {

    public static final WebSocketHeartBeatHandler INSTANCE = new WebSocketHeartBeatHandler();

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                // send a ping message
                WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] {8, 1, 8, 1}));
                ctx.writeAndFlush(frame);
            }
        }
    }
}
