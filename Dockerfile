FROM openjdk:11.0.12-jdk

#グループ、ユーザ作成、必要権限の設定
#log出力先ディレクトリを作成
#logファイルを作成
#userにlogファイルの権限を渡す
RUN groupadd apigw && \
    useradd -g apigw -m apigw && \
    mkdir -p /apigw/logs && \
    touch /apigw/logs/apigateway.log && \
    chown apigw /apigw/logs/apigateway.log

USER apigw

COPY ./target/taskappapigateway-0.0.1-SNAPSHOT.jar /apigw/apigateway.jar

CMD [ "sh", "-c", "java $JAVA_OPTIONS -jar /apigw/apigateway.jar" ] 