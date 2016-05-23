package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "users")
// Customerクラスには対応するUserクラスのフィールドを追加。
// toStringメソッドでcustomersフィールドを表示すると循環参照によってエラーになるため、
// Lombokが生成するtoStringメソッドからcustomers フィールドの出力を除く
@ToString (exclude = "customers")
public class User {
    @Id // usernameを主キーとする
    private String username;

    @JsonIgnore // REST APIでUserクラスをJSON出力する場合に、パスワード・フィールドを除外するため。パスワードは、ハッシュ化された値を格納する
    private String encodedPassword;

    // UserとCustomerを１対多の関係にするため@OneToManyアノテーションを付ける。
    // cascade = CascadeType.All することでUserの永続化操作や削除操作を関連先のCustomerにも伝播させることができる。
    // fetch = FetchType.LAZY 関連するエンティティを園地ロードさせる
    // Userエンティティを取得した際には関連するCustomerエンティティは取得されない。
    // customers フィールドにアクセスしたタイミングでCustomerエンティティが取得される
    // 双方向の関連にする場合は、mappedBy 属性に関連先でのプロパティ名を指定
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user") //
    private List<Customer> customers;
}