package com.yuma.apigateway.taskappapigateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

import javax.servlet.Filter;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.entities.TraceHeader;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

  /**
   * サンプリングルールの設定
   * 参考：https://docs.aws.amazon.com/ja_jp/xray/latest/devguide/xray-console-sampling.html
   * 実際の設定はresources/sampling-rule.ymlに定義
   **/
  static {
    try{
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
             .withSamplingStrategy(new LocalizedSamplingStrategy(
                     ResourceUtils.getURL("classpath:sampling-rule.json")));
        AWSXRay.setGlobalRecorder(builder.build());
    }catch (IOException e){
        e.printStackTrace();
    }
  }

  //トレースフィルタをアプリケーション (Spring) に追加する
  //参考：https://docs.aws.amazon.com/ja_jp/xray/latest/devguide/xray-sdk-java-filters.html#xray-sdk-java-filters-spring
  @Bean
  public Filter TracingFilter() {
    return new AWSXRayServletFilter("TaskAppApiGateway");
  }

}