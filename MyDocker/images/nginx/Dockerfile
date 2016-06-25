# image: nginx
# descr: nginx

FROM nginx

MAINTAINER caixt, xiantong0216@126.com

EXPOSE 8000

ADD nginx_8443.conf /etc/nginx/conf.d/nginx_8443.conf
ADD ssl /opt/cxt/ssl

CMD sed -i "s/#{server}/$SERVER/g" /etc/nginx/conf.d/nginx_8443.conf && \
    nginx -g 'daemon off;'