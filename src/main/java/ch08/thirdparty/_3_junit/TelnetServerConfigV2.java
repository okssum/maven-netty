package ch08.thirdparty._3_junit;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

// 지정된 클래스가 스프링의 설정 정보를 포함한 클래스임을 표시
@Configuration
// 스프링의 컨텍스트가 클래스를 동적으로 찾을 수 있도록 한다는 의미. 입력되는 패키지명을 포함한 하위 패키지를 대상으로 검색한다는 의미
@ComponentScan("ch08.thirdparty._3_junit")
// 설정 정보를 가진 파일의 위치에서 파일을 읽어서 Environment 객체로 자동 저장
@PropertySource("classpath:telnet-server.properties")
public class TelnetServerConfigV2 {
	
	@Value("${boss.thread.count}")
	private int bossCount;
	
	@Value("${worker.thread.count}")
	private int workerCount;
	
	@Value("${tcp.port}")
	private int tcpPort;

	public int getBossCount() {
		return bossCount;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public int getTcpPort() {
		return tcpPort;
	}
	
	// 객체 이름을 tcpSocketAddress로 지정
	// 이 설정은 스프링 컨텍스트에 tcpSocketAddress라는 이름으로 추가되며 다른 Bean에서 사욯할 수 있음
	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress tcpPort() {
		return new InetSocketAddress(tcpPort);
	}
	
	// @PropertySource에서 사용할 Environment 객체를 생성하는 Bean을 생성
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
