https://hub.docker.com/_/redis/


docker run -p 6379:6379 -v /root/docker/redis/redis.conf:/usr/local/etc/redis/redis.conf --privileged -d --name redis docker.io/redis redis-server /usr/local/etc/redis/redis.conf
