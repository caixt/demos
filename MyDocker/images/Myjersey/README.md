# demos/Myjersey的镜像

***
	docker build -t cxt/myjeresy:v1.0 ./
	docker run -d -p 8088:8088 -v /root/cxt/demos/images/data:/opt/cxt/Myjersey/html/data --name myjeresy cxt/myjeresy:v1.0
***
## 启动可选启动参数
* -e JAVA_OPTS="-Xms128m -Xmx512m" 
* -v /root/cxt/demos/images/data:/opt/cxt/Myjersey/html/data:ro 挂载的目录和容器共目录
* -v /home/cxt/docker/images/Myjersey/logs:opt/cxt/Myjersey/logs 挂载日志