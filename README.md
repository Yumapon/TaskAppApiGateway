# メモ

## docker コマンド

* dnmonster

    ```sh
    docker container run -p 8082:8080 -d --name dnmonster amouat/dnmonster:1.0
    ```

* jaeger tracing

    ```sh
    docker run -d --name=jaeger -p 5775:5775/udp -p 16686:16686 jaegertracing/all-in-one:latest
    #ちなみにDashBoardはhttp://localhost:16686/
    ```

* open policy agent

    ```sh
    docker run -it --rm -p 8181:8181 openpolicyagent/opa run --server --addr :8181
    ```
