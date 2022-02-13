package com.yuma.apigateway.taskappapigateway.configuration;

import java.io.IOException;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.ResourceUtils;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class XRayConfig extends AbstractXRayInterceptor{
    
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
  
    @Override    
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*Controller)")    
    public void xrayEnabledClasses() {}

}