package com.yuma.apigateway.taskappapigateway.presentation;

import java.util.List;

import com.yuma.apigateway.taskappapigateway.presentation.vo.Taskdto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPCUserInfoResponce {

    //private String saying;//諺
    private List<Taskdto> taskList;//最近登録したTask一覧
    private byte[] identidock;//dnmonsterから取得したidenticon
    
}
