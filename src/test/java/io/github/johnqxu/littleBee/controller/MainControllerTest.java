package io.github.johnqxu.littleBee.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
class MainControllerTest {

    @Autowired
    MainController mainController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void performAction() {
        mainController.setEmployExcel(new File("/home/xu/Downloads/１-人员花名册.xlsx"));
        mainController.setProjectExcel(new File("/home/xu/Downloads/２-课程信息表.xlsx"));
        mainController.setSigninExcel(new File("/home/xu/Downloads/3-参训人员名单.xlsx"));
        mainController.performAction();
    }
}