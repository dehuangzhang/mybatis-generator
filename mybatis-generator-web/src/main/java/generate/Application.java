package generate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liulang
 * @date 2018/10/29
 **/
@SpringBootApplication(scanBasePackages = {"top.sven"})
@MapperScan(basePackages ="com.ggj.generate.mapper" )
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
