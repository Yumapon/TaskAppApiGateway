package com.yuma.apigateway.taskappapigateway.Service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Task情報格納クラス
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Task {

    private String num;//一意キー
    private String name;//Task名
    private String content;//Task内容
    private LocalDate deadline;//期日
    private String client;//Task依頼主
    
}
