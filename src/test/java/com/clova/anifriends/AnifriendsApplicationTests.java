package com.clova.anifriends;

import com.clova.anifriends.base.TestContainerStarter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class AnifriendsApplicationTests extends TestContainerStarter {

    @Test
    void contextLoads() {
    }

}
