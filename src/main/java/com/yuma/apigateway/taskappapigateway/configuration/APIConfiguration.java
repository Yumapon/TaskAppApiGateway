package com.yuma.apigateway.taskappapigateway.configuration;

import com.yuma.apigateway.taskappapigateway.Service.CallApiService;
import com.yuma.apigateway.taskappapigateway.presentation.GetPCUserInfoResponce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1.0.0")
public class APIConfiguration {

    @Autowired
    CallApiService service;

    @GetMapping(path = "/getpc", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetPCUserInfoResponce PCProxyRouting(@AuthenticationPrincipal OidcUser user){

        //TODO OicdUserを好きにカスタム
        System.out.println("userinfo");
        System.out.println(user);
        String email = user.getEmail();

        //TODO: 諺取得

        //タスクを取得
        String str = service.getTasks(email);
        System.out.println(str);

        //TODO: responseを合成

        return GetPCUserInfoResponce.builder().taskList(null).build();
    }
}
