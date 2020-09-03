https://hub.docker.com/_/mysql/?

docker run -p 3306:3306 --name mysql --restart=always -v /root/docker/mysql/data:/var/lib/mysql -v /root/docker/mysql/my.cnf:/etc/mysql/conf.d/my.cnf --privileged -e MYSQL_ROOT_PASSWORD=12345678 -d docker.io/mysql

