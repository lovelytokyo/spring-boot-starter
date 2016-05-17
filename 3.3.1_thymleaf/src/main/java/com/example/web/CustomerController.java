package com.example.web;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller // 画面遷移用コントローラアノテーション
@RequestMapping("customers") // listメソッドを /customers にマッピングする
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET) // GET /customers
    String List(Model model /* 画面に値を渡すためにModelオブジェクトを使う */) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers); // 画面に渡す属性を設定

        // @Controllerを付けたコントローラのリクエスト処理メソッドの返り値は「ビュー名」＝「遷移する画面名」
        // Spring Bootではデフォルトで「classpath:templates/ + ビュー名 + .html」が画面パスとなる
        return "customers/list";
    }
}