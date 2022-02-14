FROM openjdk:11.0.12-jdk

#グループ、ユーザ作成、必要権限の設定
RUN groupadd apigw && useradd -g apigw -m apigw

USER apigw

COPY ./target/taskappapigateway-0.0.1-SNAPSHOT.jar /apigw/apigateway.jar

CMD [ "sh", "-c", "java $JAVA_OPTIONS -jar /apigw/apigateway.jar" ] 