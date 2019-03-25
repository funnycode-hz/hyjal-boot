package com.funnycode.hyjal.db.core;

import com.funnycode.hyjal.db.annotation.HyjalDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tc
 * @date 2019-03-25
 */
@Service
@HyjalDataSource(name = "local-db")
public class TestDb1MapperService {

    @Autowired
    private TestDb1Mapper testDb1Mapper;

    public String test(Long id) {
        return testDb1Mapper.getName(id);
    }

}
