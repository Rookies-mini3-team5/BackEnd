FROM openjdk:21
VOLUME /tmp
COPY ./build/libs/interview_partner-0.0.1-SNAPSHOT.jar interview_partner.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","/interview_partner.jar","--spring.profiles.active=prod"]