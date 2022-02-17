package com.yuma.apigateway.taskappapigateway.Service.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Task情報格納クラス
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {

    private String num;//一意キー
    
    private String name;//Task名

    private String content;//Task内容

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;//期日
    
    private String client;//Task依頼主
    
}
