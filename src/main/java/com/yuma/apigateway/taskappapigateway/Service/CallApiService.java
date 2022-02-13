package com.yuma.apigateway.taskappapigateway.Service;

/* dbug用
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
*/
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuma.apigateway.taskappapigateway.Service.dto.GetTaskAllResDto;
import com.yuma.apigateway.taskappapigateway.Service.dto.Task;
import com.yuma.apigateway.taskappapigateway.error.NoNormalResponseError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
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
        //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("email", email);

        //paramの値設定
        /*
        Map<String, String> params = new HashMap<>();
        params.put("email", email);

        //Requestの実施
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Task> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Task.class
        );
        */
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

        //Headerの設定
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);

        //URLの作成
        String uri = apiDestinations.getTaskapiurlGetall(); 
        //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("email", email);

        /*
        //paramの値設定
        Map<String, String> params = new HashMap<>();
        params.put("email", email);

        //Requestの実施
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<GetTaskAllResDto> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                GetTaskAllResDto.class
        );
        */

        List<Task> tasks = webClient.get().uri(uriBuilder -> uriBuilder.path(uri).queryParam("email", email).build())
                        .retrieve()
                        .bodyToFlux(Task.class)
                        .collectList()
                        .block();


        logger.info("end call getTaskAPI");
        
        return tasks;

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
        //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("size", size);

        //paramの値設定
        /*
        Map<String, String> params = new HashMap<>();
        params.put("size", String.valueOf(size));

        //Requestの実施
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                byte[].class
        );
        */

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
