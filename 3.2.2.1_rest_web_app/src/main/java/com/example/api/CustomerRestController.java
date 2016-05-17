package com.example.api;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    // 顧客新規作成
    @RequestMapping(method = RequestMethod.POST) // POST /api/customers でアクセスするとこのメソッドが実行される
    @ResponseStatus(HttpStatus.CREATED) // ResposeStatusアノテーションでAPIの通常時HTTPレスポンスが設定できる。「201 CREATED」を返却する
    Customer postCustomers(@RequestBody Customer customer /* HTTPリクエストのボティをCustomerオブジェクトにマッピングするため「RequestBodyアノテーションをつける」 */) {
        return customerService.create(customer);
    }

    // 顧客１件更新
    @RequestMapping(value="{id}", method = RequestMethod.PUT) // putCustomerメソッドに対してHTTPメソッドPUTを割り当てる
    Customer putCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        customer.setId(id);
        return customerService.update(customer);
    }

    // 顧客１件削除
    @RequestMapping(value="{id}", method = RequestMethod.DELETE) // deleteCustomerメソッドに対してHTTPメソッドDELETEを割り当てる
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 NO_CONTENTを返却する
    void deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
    }
}