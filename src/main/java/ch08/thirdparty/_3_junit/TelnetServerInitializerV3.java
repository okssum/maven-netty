package ch08.thirdparty._3_junit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetServerInitializerV3 extends ChannelInitializer<SocketChannel> {
	
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	
	private static final TelnetServerHandlerV3 SERVER_HANDLER = new TelnetServerHandlerV3();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		
		// DelimiterBasedFrameDecoder - 지정한 구분자를 기준으로 데이터를 구분해서 처리
		// 수신된 데이터의 최대 크기는 8192바이트, 해당 데이터의 마지막은 줄바꿈 문자로 구분됨
		// Delimiters.lineDelimiter() - 줄바꿈 문자가 포횜된 바이트 버퍼 객체 배열을 돌려줌
		// 윈도우 줄바꿈 문자인 CRLF와 리눅스 줄바꿈 문자인 LF가 포함되어 있음
		// 수신된 데이터가 8192바이트보다 크고 줄바꿈 문자가 포함되지 않는다면 TooLongFrameException 예외가 발생함
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast(DECODER);
		pipeline.addLast(ENCODER);
		// 기존의 데이터 핸들러들은 채널의 ChannelPipeline이 초기화될 때 새로 생성되어 파이프라인에 추가되었음
		// 여기서는 하나의 데이터 핸들러 객체를 모든 ChannelPipeline이 공유함
		// 이 같은 접근법은 채널이 생성되고 사라질 때마다 데이터 핸들러 객체를 생성하는 부하를 줄이는 데 사용됨
		// 단, ChannelPipeline에서 공유되는 데이터 핸들러는 @Sharable을 사용하여 표시해야 함
		pipeline.addLast(SERVER_HANDLER);
	}
	
}
