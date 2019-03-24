package com.funnycode.hyjal.db.prop;

import com.funnycode.hyjal.db.prop.DataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author tc
 * @date 2019-03-24
 */
@Configuration
@ConditionalOnProperty("spring.hyjal.datasource")
public class MultipleDataSourceProperties {

    private DataSourceProperties def;

    private List<DataSourceProperties> cluster;

    public DataSourceProperties getDef() {
        return def;
    }

    public void setDef(DataSourceProperties def) {
        this.def = def;
    }

    public List<DataSourceProperties> getCluster() {
        return cluster;
    }

    public void setCluster(List<DataSourceProperties> cluster) {
        this.cluster = cluster;
    }

}
