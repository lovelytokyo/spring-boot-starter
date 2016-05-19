package com.example.web;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller // 画面遷移用コントローラアノテーション
@RequestMapping("customers") // listメソッドを /customers にマッピングする
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @ModelAttribute  // コントローラ内のRequestmappingされたメソッドの前に実行され、返り値は自動でModelに追加される
    CustomerForm setUpForm() {
        return new CustomerForm(); // CusomerForm 初期化
    }

    @RequestMapping(method = RequestMethod.GET) // GET /customers
    String list(Model model /* 画面に値を渡すためにModelオブジェクトを使う */) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers); // 画面に渡す属性を設定

        // @Controllerを付けたコントローラのリクエスト処理メソッドの返り値は「ビュー名」＝「遷移する画面名」
        // Spring Bootではデフォルトで「classpath:templates/ + ビュー名 + .html」が画面パスとなる
        return "customers/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    String create(@Validated /* フォームの情報の入力チェックを行う。CustomerFormに設定したBean Validationのアノテーションが評価され、結果がBindingResultに格納される */
                          CustomerForm form, BindingResult result, Model model) {
//    String create(@Validated CustomerForm form, BindingResult result, Model model) {

        // 入力チェックの結果を確認し、エラーがある場合は一覧画面表示に戻る
        if (result.hasErrors()) {
            return list(model);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(form, customer); // CustomerFormをCustomerにコピーする
        customerService.create(customer);
        return "redirect:/customers"; // リスト画面へリダイレクト
    }
}