package com.yuma.apigateway.taskappapigateway.Service;

/* dbug用
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
*/
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
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
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * 外部API呼び出しを定義するクラス
 */
@Service
@AllArgsConstructor
public class CallApiService {

    //API呼び出し用クラス
    @Autowired
    private WebClient webClient;

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

        //Headerの設定
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //URiの作成
        String uri = apiDestinations.getTaskapiurlGet() + "/" + id; 
 
        Task task = webClient.get().uri(uriBuilder -> uriBuilder.path(uri).queryParam("email", email).build())
                        .retrieve()
                        .bodyToMono(Task.class)
                        .block();


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

                    tasks = mapper.readValue(resStr, GetTaskAllResDto.class);

                    //Debug用
                    System.out.println("##########Debug#########");
                    System.out.println(entity);
                    //Debug用
                    System.out.println("##########Debug2#########");
                    System.out.println(resStr);
                    //Debug用
                    System.out.println("##########Debug3#########");
                    System.out.println(tasks);
                }
                else {

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

        //Headerの設定
        //HttpHeaders headers = new HttpHeaders();

        //URLの作成
        String uri = apiDestinations.getDnmonsterGet() + "/" + email; 

        byte[] image = DataBufferUtils.join(webClient.get().uri(uriBuilder -> uriBuilder.path(uri).queryParam("size", size).build())
                        .retrieve()
                        .bodyToFlux(DataBuffer.class))
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            return bytes;
                        })
                        .block();

        return image;
    }
    
}
