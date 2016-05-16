package com.example;

import com.example.app.Argument;
import com.example.app.ArgumentResolver;
import com.example.app.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan // このクラスと同じパッケージ以下のクラスを走査。
public class App implements CommandLineRunner /* CommandLineRunnerインタフェースのrun メソッドがFrontendクラスのrunに相当 */{
    @Autowired // CommandLineRunner インタフェースをもつクラスは、Dependency Injectionが可能
    ArgumentResolver argumentResolver;
    @Autowired
    Calculator calculator;

    @Override
    public void run(String... strings) throws Exception {
        System.out.print("Enter 2 numbers like 'a b' : ");
        Argument argument = argumentResolver.resolve(System.in);
        int result = calculator.calc(argument.getA(), argument.getB());
        System.out.println("result =" + result);
    }

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
