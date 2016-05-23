package com.example.web;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import com.example.service.LoginUserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
                          CustomerForm form,
                  BindingResult result,
                  Model model,
                  @AuthenticationPrincipal LoginUserDetails userDetails ) {

        // 入力チェックの結果を確認し、エラーがある場合は一覧画面表示に戻る
        if (result.hasErrors()) {
            return list(model);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(form, customer);
        customerService.create(customer, userDetails.getUser()); // ログイン中のLoginUserDetailsオブジェクトの中に格納されているUser情報を取得してCustomerServiceに渡す
        return "redirect:/customers";
    }

    @RequestMapping(value = "edit", params = "form", method = RequestMethod.GET)
    String editForm(@RequestParam /* 引数にRequestParamアノテーションを付けることで、特定のリクエスト・パラメータをマッピングできる */
                            Integer id,
                    CustomerForm form) {
        Customer customer = customerService.findOne(id);
        BeanUtils.copyProperties(customer, form); // 顧客編集フォームに現在の顧客情報を表示するため、CustomerService#findOneで取得した顧客情報をCustomerFormにコピーする
        return "customers/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    String edit(@RequestParam Integer id,
                @Validated CustomerForm form,
                BindingResult result,
                @AuthenticationPrincipal LoginUserDetails userDetails){
        if (result.hasErrors()) {
            return editForm(id, form);
        }

        Customer customer = new Customer();
        BeanUtils.copyProperties(form, customer);
        customer.setId(id);
        customerService.update(customer, userDetails.getUser());
        return "redirect:/customers";
    }

    // (4)
    @RequestMapping(value = "edit", params = "goToTop")
    String goToTop() {
        return "redirect:/customers";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    String delete(@RequestParam Integer id) {
        customerService.delete(id);
        return "redirect:/customers";
    }
}