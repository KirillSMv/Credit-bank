package ru.development.deal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, ports = {9094})
class DealApplicationTests {

    @Test
    void contextLoads() {
    }

}
