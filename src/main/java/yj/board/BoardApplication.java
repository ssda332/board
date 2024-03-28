package yj.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import yj.board.util.JwtProperties;
import yj.board.util.MyOAuth2Properties;

@EnableJpaAuditing
@EnableConfigurationProperties({JwtProperties.class, MyOAuth2Properties.class})
@SpringBootApplication
//@MapperScan("yj.board.repository.mybatis.mapper") // MyBatis 매퍼 인터페이스가 위치한 패키지를 지정
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
