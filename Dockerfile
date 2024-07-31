# base image
FROM openjdk:17-slim
# ./build/libs/*T.jar을 app.jar로 copy
COPY ./build/libs/*T.jar app.jar
RUN java -version
# ENV PROFILES=postgres
CMD ["java", "-jar", "-Dspring.profiles.active=${profiles}", "app.jar"]
# profiles 환경 변수를 받는다. docker run할 때 환경변수로 넣어준다.
EXPOSE 8080
# port 번호를 지정해놓는다.
