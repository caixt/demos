# image: Myjersey
# descr: jerseymain-0.0.1-SNAPSHOT-bin.tar.gz

FROM registry.aliyuncs.com/daydayup/oracle-jre8

MAINTAINER caixt, xiantong0216@126.com

EXPOSE 8088
ENV LANG en_US.UTF-8

ADD jerseymain-0.0.1-SNAPSHOT-bin.tar.gz /opt/cxt/
RUN mv /opt/cxt/jerseymain-0.0.1-SNAPSHOT /opt/cxt/Myjersey


CMD foreground=true /opt/cxt/Myjersey/bin/start.sh 