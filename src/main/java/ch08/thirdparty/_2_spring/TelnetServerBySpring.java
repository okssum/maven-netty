package ch08.thirdparty._2_spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class TelnetServerBySpring {

	public static void main(String[] args) {
		
		// 어노테이션 기반의 스프링 컨텍스트 객체 생성
		AbstractApplicationContext springContext = null;
		
		try {
			// 어노테이션 설정을 가진 클래스 지정
			// 여기서 입력되는 클래스는 스프링 컨텍스트를 생성하는 데 필요한 설정 정보가 포함되어 있음
			springContext = new AnnotationConfigApplicationContext(TelnetServerConfig.class);
			springContext.registerShutdownHook();
			
			// 스프링 컨텍스트에서 TelnetServerV2 클래스의 객체를 가져옴
			TelnetServerV2 server = springContext.getBean(TelnetServerV2.class);
			server.start();
		}
		finally {
			springContext.close();
		}
	}

}
