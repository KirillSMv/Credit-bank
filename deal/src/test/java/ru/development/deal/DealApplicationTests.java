package ru.development.deal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@EmbeddedKafka(partitions = 1, ports = {9094})
class DealApplicationTests {

    @Test
    void contextLoads() {
        //Загрузка контекста со всеми бинами
    }
}
