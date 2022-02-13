FROM openjdk:11.0.12-jdk

RUN groupadd -r apigw && useradd -r -g apigw apigw

COPY ./target/taskappapigateway-0.0.1-SNAPSHOT.jar /root/apigateway.jar

USER apigw

CMD [ "sh", "-c", "java $JAVA_OPTIONS -jar /root/apigateway.jar" ] 