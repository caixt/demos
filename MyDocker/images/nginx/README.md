# demos/Myjersey的镜像

***
	docker build -t cxt/nginx:v1.0 ./
	docker run -d -p 8443:8443 -e SERVER=192.168.133.131:8088 --name myninx cxt/nginx:v1.0

* 192.168.133.131 宿主机ip
* 查看日志 docker logs 容器id
* [error] 7#7: *1 connect() failed (113: No route to host) 宿主机的防火墙没关