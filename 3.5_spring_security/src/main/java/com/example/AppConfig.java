package com.example;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    // 「データ・ソースの設定を明示的に変更」で設定したデータ・ソース用のプロパティ群を格納するクラスをインジェクション
    @Autowired
    DataSourceProperties dataSourceProperties;
    DataSource dataSource;

    @Bean
    DataSource realDataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create(this.dataSourceProperties.getClassLoader())
                .url(this.dataSourceProperties.getUrl())
                .username(this.dataSourceProperties.getUsername())
                .password(this.dataSourceProperties.getPassword());
        this.dataSource = factory.build();
        return this.dataSource;
    }

    @Bean
    DataSource dataSource() {
        return new Log4jdbcProxyDataSource(this.dataSource);
    }

    // OrderアノテーションでBean定義の優先順位を指定できる。Filterの場合ｈ，Orderの値の適用の順番になる
    // 文字コードの設定は最初に行う必要があるため、Ordered.HIGHEST_PRECEDENT(=Integer.MIN_VALUE)を設定して優先させる
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8"); // リクエスト文字コードがUTF-8になるように設定
        filter.setForceEncoding(true);
        return filter;
    }


}