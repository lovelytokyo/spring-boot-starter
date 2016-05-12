package com.example.app;

import lombok.Data;

@Data // コンパイル時、各フィールドのsetter/getter, toStringメソッド、equalsメソッド、hashCodeメソッドが生成される
public class Argument {
    private final int a;
    private final int b;
}