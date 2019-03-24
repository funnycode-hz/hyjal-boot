package com.funnycode.hyjal.db.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private TestDb1Mapper testDb1;

    @Autowired
    private TestDb2Mapper testDb2;
    
    @Test
    public void test12() throws IOException {
        System.out.println(testDb2.getName(1));
        System.in.read();
    }

}