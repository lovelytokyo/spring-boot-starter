package com.example.api;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // REST Webサービスのエンド・ポイントであるControllerクラス
@RequestMapping("api/customers") // パスルートを設定するアノテーション
public class CustomerRestController {
    @Autowired // 作成済みCustomerServiceをDIする
    CustomerService customerService;

    // 顧客全件取得
    // getCustomersメソッドに対してHTTP GETメソッド割当て。api/customersにアクセスされるとgetCustomersメソッドが実行される
    @RequestMapping(method = RequestMethod.GET)
    List<Customer> getCustomers() {
        List<Customer> customers = customerService.findAll();
        // @RequestMapping アノテーションを付けたメソッドの返り値はシリアライズされてHTTPレスポンスのボティに設定される
        // デフォルトでは、JAVAオブジェクトはJSON形式でシリアライズされる
        return customers;
    }

    // 顧客１件取得
    // getCustomerメソッドに対してもGETメソッドを割り当てる
    // value属性に相対パスを設定。GET /api/customer/{id} にアクセスすると getCustomerメソッドが実行される
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    Customer getCustomer(@PathVariable Integer id) {
        Customer customer = customerService.findOne(id);
        return customer;
    }

}