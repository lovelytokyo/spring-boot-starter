package com.example.service;

import com.example.domain.User;
import lombok.Data;
import org.springframework.security.core.authority.AuthorityUtils;

@Data
public class LoginUserDetails extends org.springframework.security.core.userdetails.User
        /* UserDetailsのデフォルト実装であるorg.springframework.secutiry.core.userdetails.Userクラスを拡張する */
{
    private final User user; // Spring Securityの認証ユーザから実際のUserオブジェクトを取得できるようにフィールド追加する

    public LoginUserDetails(User user) {
        // org.springframework.secutiry.core.userdetails.Userクラスのコンストラクタを使って
        // ユーザ名、パスワード、認可用のロールを設定する
        // ロール作成には、AuthorityUtilsを使うと便利
        super(user.getUsername(), user.getEncodedPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.user = user;
    }
}