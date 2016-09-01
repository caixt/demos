# demos/Myjersey的镜像

***
	docker build -t cxt/nginx:v1.0 ./
	docker run -d -p 8443:8443 --volumes-from *** --link ***:MYJERSEY --name myninx cxt/nginx:v1.0
	*** 为myjersey的容器名称
	
* /root/cxt/demos/images/data 挂载
* -v /root/cxt/demos/images/data:/opt/cxt/Myjersey/html/data:ro 挂载的目录和容器共目录
* 查看日志 docker logs 容器id
* [error] 7#7: *1 connect() failed (113: No route to host) 宿主机的防火墙没关