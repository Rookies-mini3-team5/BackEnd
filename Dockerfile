FROM openjdk:21
VOLUME /tmp
COPY ./build/libs/interview_partner-0.0.1-SNAPSHOT-plain.jar interview_partner.jar
ENTRYPOINT ["java","-jar","/interview_partner.jar","--spring.profiles.active=prod","--DB_HOST=mysql-svc"]