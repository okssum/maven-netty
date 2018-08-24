package ch08.thirdparty._1_maven;

import java.net.InetAddress;
import java.util.Date;

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
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
	
	// 각각 클라이언트 접속 완료를 알리는 이벤트
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		ctx.write("환영합니다. " + InetAddress.getLocalHost().getHostName() + " 서버에 접속 하셨습니다!\r\n");
		ctx.write("현재 시간은 " + new Date() + " 입니다.\r\n");
		// 채널에 기록된 데이터를 즉시 클라이언트로 전송
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		
		String response;
		boolean close = false;
		
		System.out.println(request);
		
		if (request.isEmpty()) {
			response = "명령을 입력해주세요.\r\n";
		} else if ("bye".equals(request.toLowerCase())) {
			response = "안녕히 가세요!\r\n";
			close = true;
		} else {
			response = "입력하신 명령은 '" + request + "' 입니다.\r\n";
		}
		
		ChannelFuture future = ctx.write(response);
		
		// 연결된 클라이언트 채널 비동기로 닫기
		if (close) {
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
