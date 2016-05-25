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
    @Column(nullable = false) // 該当するDBカラムに対する「名前」や「制約」を設定する
    private String firstName;
    @Column(nullable = false)
    private String lastName;
}
