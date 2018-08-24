package ch08.thirdparty._3_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

/**
 * @author  sunok
 * @Comment EmbeddedChannel를 사용하여 DelimiterBasedFrameDecoder 클래스를 테스트하기
 * 			EmbeddedChannel - 채널 파이프라인 및 이벤트 루프 설정과 같은 부가작업 없이 순수하게 이벤트 핸들러를 테스트할 수 있음
 */
public class DelimiterBasedFrameDecoderTest {

	@Test
	public void testDecoder() {
		
		String writeData = "안녕하세요\r\n반갑습니다\r\n";
		String firstResponse = "안녕하세요\r\n";
		String secondResonse = "반갑습니다\r\n";
		
		// 최대 8192 바이트의 데이터를 줄바꿈 문자를 기준으로 잘라서 디코딩하는 DelimiterBasedFrameDecoder 객체 생성
		// 두 번째 인수는 디코딩된 데이터에 구분자의 포함 여부를 설정함. 여기서는 디코딩된 문자열에 줄바꿈 문자가 포함되지 않음
		DelimiterBasedFrameDecoder decoder = new DelimiterBasedFrameDecoder(8192, false, Delimiters.lineDelimiter());
		// 위에서 생성한 DelimiterBasedFrameDecoder 객체 등록
		EmbeddedChannel embeddedChannel = new EmbeddedChannel(decoder);
		
		ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes());
		// 바이트 버퍼로 변환된 문자열을 EmbeddedChannel의 인바운드에 기록
		// 즉, 클라이언트로부터 데이터를 수신한 것과 같은 상태가 됨
		boolean result = embeddedChannel.writeInbound(request);
		// 수행 결과가 정상인지 확인
		assertTrue(result);
		
		ByteBuf response = null;
		// EmbeddedChannel에서 인바운드 데이터를 읽음. 즉, 디코더가 수신하여 디코딩한 데이터를 조회함
		response = (ByteBuf) embeddedChannel.readInbound();
		assertEquals(firstResponse, response.toString(Charset.defaultCharset()));
		
		response = (ByteBuf) embeddedChannel.readInbound();
		assertEquals(secondResonse, response.toString(Charset.defaultCharset()));
		
		// EmbeddedChannel 종료
		embeddedChannel.finish();
	}

}
