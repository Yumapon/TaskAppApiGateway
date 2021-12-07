package com.yuma.apigateway.taskappapigateway.presentation.vo;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Taskdto {

    private final String num;//一意キー
    private final String name;//Task名
    private final String content;//Task内容
    private final LocalDate deadline;//期日
    private final String client;//Task依頼主
    
}
