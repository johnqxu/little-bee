package io.github.johnqxu.littleBee.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SigninServiceTest {

    @Autowired
    SigninService signinService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testAssign() {
        signinService.assign();
    }
}