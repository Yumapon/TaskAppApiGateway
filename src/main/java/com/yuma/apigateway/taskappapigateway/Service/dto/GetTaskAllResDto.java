package com.yuma.apigateway.taskappapigateway.Service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * GetALLtask APIを呼び出したレスポンスを取得するクラス
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class GetTaskAllResDto {

    //Task一覧
    private List<Task> taskList;
    
}
