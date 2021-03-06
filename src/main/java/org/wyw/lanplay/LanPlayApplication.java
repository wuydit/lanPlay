package org.wyw.lanplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author wuyd.wuyd
 */
@SpringBootApplication
public class LanPlayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LanPlayApplication.class, args);
    }

}
