# image: Myjersey
# descr: build-0.0.1-SNAPSHOT-all.tar.gz

FROM registry.aliyuncs.com/daydayup/oracle-jre8

MAINTAINER caixt, xiantong0216@126.com

EXPOSE 8088

ADD build-0.0.1-SNAPSHOT-all.tar.gz /opt/cxt/
RUN mv /opt/cxt/build-0.0.1-SNAPSHOT /opt/cxt/Myjersey


CMD foreground=true /opt/cxt/Myjersey/bin/start.sh 