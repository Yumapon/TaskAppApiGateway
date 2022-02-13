package com.yuma.apigateway.taskappapigateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.entities.TraceHeader;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

  @Bean
  public Filter TracingFilter() {
    return new AWSXRayServletFilter("TaskAppApiGateway");
  }

  @Bean
  //WebClientに共通して行うフィルタ処理を定義、実際の処理はexchangeFunction()にて定義
  public WebClient userWebClient(){
      return WebClient.builder()
              .baseUrl("")
              .filter(exchangeFilterFunction())
              .build();
  }

  //com.amazonaws.xray.AWSXRayから実行中のセグメントオブジェクトを取得します。
  private ExchangeFilterFunction exchangeFilterFunction(){
      return (clientRequest, nextFilter) -> {
          //セグメント情報を取得
          Segment segment = AWSXRay.getCurrentSegment();
          Subsegment subsegment = AWSXRay.getCurrentSubsegment();
          //Traceにヘッダーを追加
          TraceHeader traceHeader = new TraceHeader(segment.getTraceId(),
                  segment.isSampled() ? subsegment.getId() : null,
                  segment.isSampled() ? TraceHeader.SampleDecision.SAMPLED : TraceHeader.SampleDecision.NOT_SAMPLED);

          ClientRequest newClientRequest = ClientRequest.from(clientRequest)
                  .header(TraceHeader.HEADER_KEY, traceHeader.toString())
                  .build();
          return nextFilter.exchange(newClientRequest);
      };
  }
  
}