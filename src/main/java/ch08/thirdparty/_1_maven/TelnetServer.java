package ch08.thirdparty._1_maven;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TelnetServer {
	
	// 기존 텔넷 서버 23번 사용. 포트 충돌 방지
	private static final int PORT = 8023;
	
	public static void main(String[] args) throws Exception {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 .handler(new LoggingHandler(LogLevel.INFO))
			 .childHandler(new TelnetServerInitializer());
			
			// bind 메서드 처리가 완료될 때까지 대기. 처리 완료되면 생성된 서버 채널에 대한 ChannelFuture 객체를 돌려줌
			ChannelFuture future = b.bind(PORT).sync();
			// ChannelFuture 객체가 참조하는 서버 채널에 close 이벤트가 발생할 때까지 대기
			future.channel().closeFuture().sync();
		} 
		finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
