package ch08.thirdparty._3_junit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

// @Sharable
// 1. 여러 파이프라인 사이에서 공유 가능하다는 표시
// 2. 스레드 안정성을 지원해야 함(멤버 변수를 사용하지 않아야 함)
//
//SimpleChannelInboundHandler<String>
//여기에 지정된 제너릭 타입은 데이터 수신 이벤트인 channelRead0 메서드의 두번째 인수 데이터형이 됨(수신된 데이터가 String 데이터임을 의미)
@Sharable
public class TelnetServerHandlerV3 extends SimpleChannelInboundHandler<String> {
	
	// 각각 클라이언트 접속 완료를 알리는 이벤트
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		// 새로운 클라이언트가 접속되었을 때 환영메시지를 생성하고 그 결과를 클라이언트로 전송
		// 입력 파라미터오 상관없이 메시지를 생성하므로 정적 메서드로 선언
		ctx.write(ResponseGenerator.makeHello());
		// 채널에 기록된 데이터를 즉시 클라이언트로 전송
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		
		ResponseGenerator generator = new ResponseGenerator(request);
		// 사용자가 입력한 메시지에 해당하는 응답 문자열 생성
		String response = generator.response();
		ChannelFuture future = ctx.write(response);
		
		// 연결된 클라이언트 채널 비동기로 닫기
		// 사용자가 입력한 메시지가 연결 종류 문자열인지 확인
		if (generator.isClose()) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	// channelRead0 이벤트가 완료되면 호출되는 이벤트
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
		// 채널에 기록된 데이터를 즉시 클라이언트로 전송
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		cause.printStackTrace();
		ctx.close();
	}
	
}
