package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity // @JPAのEntityであることを示す
@Table (name = "customers") // エンティティに対応するテーブル名を指定。デフォルトでは、テーブル名＝クラス名。
@Data
@NoArgsConstructor // JPAの仕様でエンティティ・クラスは引数のない「デフォルト・コンストラクタ」が必要。Lombokでデフォルト・コンストラクタを生成させる
@AllArgsConstructor // JPAとは関係ないが、プログラミングの祭に便利なので、全フィールドを引数にもつコンストラクタもLombokで生成させる
public class Customer {
    @Id // エンティティの主キーであるフィールドに「@Id」アノテーションをつける
    @GeneratedValue // 主キーがDBで自動採番されることを示す
    private Integer id;
    private String firstName;
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY) // UserとCustomerを多対1にするため
    @JoinColumn(nullable = true, name = "username") // 外部キーカラム名を指定
    private User user;
}
