package com.example.web;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data // LombokおDataアノテーションを使って、setter/getter実装を省略する
public class CustomerForm {
    @NotNull // 入力チェックアノテーション
    @Size(min=1, max=127) // １文字以上、１２７文字以下
    private String firstName;
    @NotNull
    @Size(min=1, max=127)
    private String lastName;
}