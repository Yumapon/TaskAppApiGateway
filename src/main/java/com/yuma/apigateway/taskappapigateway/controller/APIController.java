package com.yuma.apigateway.taskappapigateway.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yuma.apigateway.taskappapigateway.Service.CallApiService;
import com.yuma.apigateway.taskappapigateway.Service.dto.Task;
import com.yuma.apigateway.taskappapigateway.error.NoNormalResponseError;
import com.yuma.apigateway.taskappapigateway.presentation.GetPCUserInfoResponce;
import com.yuma.apigateway.taskappapigateway.presentation.vo.Taskdto;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * API定義
 * APIGatewayのAPIを定義している
 * 
 * 本当は流量制御もしたいけど、現状API合成と認証のみ。
 * TODO メトリクスもここで実装しておきたい
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/v1.0.0")
public class APIController {

    @Autowired
    CallApiService service;

    @GetMapping(path = "/getpc", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetPCUserInfoResponce PCProxyRouting(@AuthenticationPrincipal OidcUser user) throws NoNormalResponseError, FileNotFoundException, IOException{

        //OicdUserからEmailを取得(呼び出すAPIに必要なため)
        String email = user.getEmail();

        //TODO: 諺取得

        //タスクを取得
        List<Task> tasklist = service.getAllTasks(email);

        //Serviceから取得したtaskをフロント用に加工
        List<Taskdto> taskdto = new ArrayList<>();
        for(Task t : tasklist){
            Taskdto td = Taskdto.builder()
                                .num(t.getNum())
                                .name(t.getName())
                                .content(t.getContent())
                                .deadline(t.getDeadline())
                                .client(t.getClient())
                                .build();
            taskdto.add(td);
        }

        //identidockを取得
        byte[] identidock = service.getDnMonster(email, 80);

        //TODO: responseを合成
        GetPCUserInfoResponce getpcInfo = GetPCUserInfoResponce.builder()
                                                                .taskList(taskdto)
                                                                .identidock(identidock)
                                                                .build();

        return getpcInfo;
    }

    @GetMapping(path = "test")
    public void testMethod(){
        System.out.print("ok");
    }

}
