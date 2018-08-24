package ch08.thirdparty._3_junit;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.base64.Base64Encoder;

public class Base64EncoderTest {

	@Test
	public void testEncoder() {
		
		String writeData = "안녕하세요";
		ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes());
		
		Base64Encoder encoder = new Base64Encoder();
		// EmbeddedChannel에 Base64Encoder 객체 등록
		EmbeddedChannel embeddedChannel = new EmbeddedChannel(encoder);
		
		// 아웃바운드에 데이터를 기록
		embeddedChannel.writeOutbound(request);
		// Base64Encoder의 인코딩 결과 조회
		ByteBuf response = (ByteBuf) embeddedChannel.readOutbound();
		
		// "안녕하세요" 문자열을 Base64로 인코딩한 값을 선언
		String expect = "7JWI64WV7ZWY7IS47JqU";
		assertEquals(expect, response.toString(Charset.defaultCharset()));
		
		embeddedChannel.finish();
	}

}
