# image: nginx
# descr: nginx

FROM nginx

MAINTAINER caixt, xiantong0216@126.com

EXPOSE 8443
ENV LANG en_US.UTF-8

ADD nginx_8443.conf /etc/nginx/conf.d/nginx_8443.conf
ADD ssl /opt/cxt/ssl
ADD jerseymain-0.0.1-SNAPSHOT-bin.tar.gz /opt/cxt/
RUN ln -fs /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
	mv /opt/cxt/jerseymain-0.0.1-SNAPSHOT /opt/cxt/Myjersey

CMD SERVER=`echo $MYJERSEY_PORT| awk -F// '{print $2}'` && \
	sed -i "s/#{server}/$SERVER/g" /etc/nginx/conf.d/nginx_8443.conf && \
    nginx -g 'daemon off;'