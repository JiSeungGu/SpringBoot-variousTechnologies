package org.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:/test.properties")
@SpringBootTest
public class SpringinitApplicationTests {

    Environment environment;

    @Test
     public void contextLoads() {
        assertThat(environment.getProperty("sung.name"))
                .isEqualTo("seung");
    }
}
