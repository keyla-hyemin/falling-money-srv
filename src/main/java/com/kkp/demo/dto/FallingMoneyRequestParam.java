package com.kkp.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FallingMoneyRequestParam {
    private int money;  //뿌릴 금액
    private int personnel;  //뿌릴 인원
}
