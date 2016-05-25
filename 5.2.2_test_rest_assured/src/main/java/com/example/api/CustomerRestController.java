package com.example.api;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.net.URI;
import java.util.List;

@RestController // REST Webサービスのエンド・ポイントであるControllerクラス
@RequestMapping("api/customers") // パスルートを設定するアノテーション
public class CustomerRestController {
    @Autowired // 作成済みCustomerServiceをDIする
    CustomerService customerService;

    // 顧客全件取得
    // getCustomersメソッドに対してHTTP GETメソッド割当て。api/customersにアクセスされるとgetCustomersメソッドが実行される
    // page, size リクエスト・パラメータはPageableオブジェクトにマッピングされる
    @RequestMapping(method = RequestMethod.GET)
    Page<Customer> getCustomers(@PageableDefault Pageable pageable /* ページネーション情報 */) {
        Page<Customer> customers = customerService.findAll(pageable); // ページング情報をCustomerServiceへ渡す
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
    ResponseEntity<Customer> postCustomers(@RequestBody Customer customer /* HTTPリクエストのボティをCustomerオブジェクトにマッピングするため「RequestBodyアノテーションをつける」 */
        , UriComponentsBuilder uriBuilder /* コンテキスト・パス相対のURIを構築するのに便利 */) {

        Customer created = customerService.create(customer);
        URI location = uriBuilder.path("api/customers/{id}")
                .buildAndExpand(created.getId())
                .toUri(); // UriComponentsBuilderと作成したCustomerオブジェクトのIDを用いてリソースURIを作る

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        // HTTPレスポンスヘッダーを設定したい場合は、ResposeEntityオブジェクトを返却する
        return new ResponseEntity<>(created, headers, HttpStatus.CREATED);

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