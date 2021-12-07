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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;

/**
 * 外部API呼び出しを定義するクラス
 */
@Service
@AllArgsConstructor
public class CallApiService {

    //API呼び出し用クラス
    @Autowired
    private RestTemplate restTemplate;

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

        //URLの作成
        String url = apiDestinations.getTaskapiurlGet() + "/" + id; 
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("email", email);

        //paramの値設定
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

        //statesCodeを判断
        if(response.getStatusCodeValue() == 200){
            //取得したtaskを返却
            return response.getBody();
        }
        //TODO 本当はStatusCodeごとにハンドリングすべきだけど、面倒なのでしない
        else{
            throw new NoNormalResponseError();
        }
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
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);

        //URLの作成
        String url = apiDestinations.getTaskapiurlGetall(); 
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("email", email);

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

        //statesCodeを判断
        if(response.getStatusCodeValue() == 200){
            //取得したtaskを返却
            //return Arrays.asList(response.getBody());
            return response.getBody().getTaskList();
        }
        //TODO 本当はStatusCodeごとにハンドリングすべきだけど、面倒なのでしない
        else{
            throw new NoNormalResponseError();
        }

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
        HttpHeaders headers = new HttpHeaders();

        //URLの作成
        String url = apiDestinations.getDnmonsterGet() + "/" + email; 
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("size", size);

        //paramの値設定
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

        //statesCodeを判断
        if(response.getStatusCodeValue() == 200){
            //debug用
            //byte[] identidock = Base64.getEncoder().encode(response.getBody());
            //byte[] data = Base64.getDecoder().decode(identidock);
            /*
            try (OutputStream stream = new FileOutputStream("logs/test.png")) {
                stream.write(data);
            }
            //取得したidentidockをBase64エンコード
            return identidock;
            */
            return Base64.getEncoder().encode(response.getBody());
        }
        //TODO 本当はStatusCodeごとにハンドリングすべきだけど、面倒なのでしない
        else{
            throw new NoNormalResponseError();
        }   

    }
    
}
