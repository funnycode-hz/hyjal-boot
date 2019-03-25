package com.funnycode.hyjal.db.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author tc
 * @date 2019-03-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultipleDataSourceApplication.class)
public class MultipleDataSourceTest {

    @Autowired
    private Environment environment;

    @Autowired
    private TestDb1Mapper testDb1;

    @Autowired
    private TestDb2Mapper testDb2;

    @Autowired
    private TestDb1MapperService testDb1MapperService;

    @Test
    public void test12() throws IOException {
        System.out.println(" testDb2 test : " + testDb2.getName(1));
        System.out.println(" testDb1 test : " + testDb1.getName(11L));
        System.out.println(" testDb1MapperService test : " + testDb1MapperService.test(11L));
        System.in.read();
    }

}