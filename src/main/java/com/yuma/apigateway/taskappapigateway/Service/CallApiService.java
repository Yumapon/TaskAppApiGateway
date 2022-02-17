package com.yuma.apigateway.taskappapigateway.Service;

/* dbug用
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
*/
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuma.apigateway.taskappapigateway.Service.dto.GetTaskAllResDto;
import com.yuma.apigateway.taskappapigateway.Service.dto.Task;
import com.yuma.apigateway.taskappapigateway.error.NoNormalResponseError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

/**
 * 外部API呼び出しを定義するクラス
 */
@Service
@AllArgsConstructor
public class CallApiService {
    
    //API情報の定義クラス
    @Autowired
    private APIDestinations apiDestinations;

    //Loggerクラス
    private static final Logger logger = LoggerFactory.getLogger(CallApiService.class);

    /**
     * TaskAPIを呼びだす
     * 
     * @param email
     * @return
     * @throws NoNormalResponseError
     */
    public Task getTask(String id, String email) throws NoNormalResponseError{

        logger.info("start call getTask API");

        //返すタスククラス
        Task task = null;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build(); ){

            //URIの作成
            URI uri = new URIBuilder(apiDestinations.getTaskapiurlGet() + "/" + id)
                        .addParameter("email", email)
                        .build(); 

            //Segmentの情報を取得
            Segment segment = AWSXRay.getCurrentSegment();
            String traceId = segment.getTraceId().toString();
            logger.debug("TRACEID: " + traceId);
            
            //HTTPリクエストの設定
            //一応タイムアウト設定を実装
            RequestConfig config = RequestConfig.custom()
                                                .setSocketTimeout(3000)
                                                .setConnectTimeout(3000)
                                                .build();

            // HTTPのGETリクエストを構築
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("trace-id", traceId);
            httpGet.setConfig(config);

            //HTTPリクエストを実行する。HTTPステータスが200の場合のみOKとする
            try( CloseableHttpResponse response = httpClient.execute(httpGet); ){

                logger.debug("StatusCode: " + response.getStatusLine().getStatusCode());

                if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                    HttpEntity entity = response.getEntity();
                    String resStr = EntityUtils.toString(entity);
                    logger.debug("JsonString: " + resStr);
                    
                    ObjectMapper mapper = new ObjectMapper();

                    task = mapper.readValue(resStr, Task.class);
                }
                else {
                    logger.error("Status Codeが200以外で帰ってきました");
                    task = new Task();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        logger.info("end call getTaskAPI");
        
        return task;

    }

    /**
     * 
     * TaskAPIのTask一覧を取得するAPIを呼び出す
     *  
     * @param email
     * @return
     * @throws NoNormalResponseError
     */
    public List<Task> getAllTasks(String email) throws NoNormalResponseError{
        
        logger.info("start call getallTask API");

        //返すタスク一覧
        GetTaskAllResDto tasks = null;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build(); ){

            //URIの作成
            URI uri = new URIBuilder(apiDestinations.getTaskapiurlGetall())
                        .addParameter("email", email)
                        .build(); 

            //Segmentの情報を取得
            Segment segment = AWSXRay.getCurrentSegment();
            String traceId = segment.getTraceId().toString();
            logger.debug("TRACEID: " + traceId);
            
            //HTTPリクエストの設定
            //一応タイムアウト設定を実装
            RequestConfig config = RequestConfig.custom()
                                                .setSocketTimeout(3000)
                                                .setConnectTimeout(3000)
                                                .build();

            // HTTPのGETリクエストを構築
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("trace-id", traceId);
            httpGet.setConfig(config);

            //HTTPリクエストを実行する。HTTPステータスが200の場合のみOKとする
            try( CloseableHttpResponse response = httpClient.execute(httpGet); ){

                logger.debug("StatusCode: " + response.getStatusLine().getStatusCode());

                if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                    HttpEntity entity = response.getEntity();
                    String resStr = EntityUtils.toString(entity);
                    logger.debug("JsonString: " + resStr);
                    
                    ObjectMapper mapper = new ObjectMapper();

                    tasks = mapper.readValue(resStr, GetTaskAllResDto.class);
                }
                else {
                    logger.error("Status Codeが200以外で帰ってきました");
                    tasks = new GetTaskAllResDto();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        logger.info("end call getTaskAPI");
        
        return tasks.getTaskList();

    }

    /**
     * identidockを取得する
     * 
     * @param email
     * @param size
     * @return 
     * @throws NoNormalResponseError
     * @throws IOException
     * @throws FileNotFoundException
     */
    public byte[] getDnMonster(String email, int size) throws NoNormalResponseError, FileNotFoundException, IOException{

        logger.info("start call dnmonster api");

        byte[] identidock = null;

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build(); ){

            //URIの作成
            URI uri = new URIBuilder(apiDestinations.getDnmonsterGet() + "/" + email)
            .addParameter("size", String.valueOf(size) )
            .build();

            //HTTPリクエストの設定
            //一応タイムアウト設定を実装
            RequestConfig config = RequestConfig.custom()
                                                .setSocketTimeout(3000)
                                                .setConnectTimeout(3000)
                                                .build();
            
            // HTTPのGETリクエストを構築
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(config);

            //HTTPリクエストを実行する。HTTPステータスが200の場合のみOKとする
            try( CloseableHttpResponse response = httpClient.execute(httpGet); ){

                if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
                    HttpEntity entity = response.getEntity();
                    String resStr = EntityUtils.toString(entity);
                    
                    ObjectMapper mapper = new ObjectMapper();

                    identidock = mapper.readValue(resStr, byte[].class);
                }
                else {
                    logger.error("Status Codeが200以外で帰ってきました");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return identidock;
    }
    
}
