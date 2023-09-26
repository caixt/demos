https://hub.docker.com/_/mongodb/

docker run -p 27017:27017 --name mongo --privileged=true -v /root/docker/mongodb/data/:/data/db -d docker.io/mongo
docker run -p 27017:27017 --name mongo --privileged=true -e MONGO_INITDB_ROOT_USERNAME=admin -v /root/docker/mongodb/data/:/data/db -d docker.io/mongo





1.
docker run -d -p 27017:27017 --privileged=true --name mongo docker.io/mongo --replSet=repl

2.
docker exec -it mongo /bin/bash
mongo 或 mongosh
rs.initiate({"_id":"repl", members:[{"_id":0, "host":"127.0.0.1:27017"}]});
rs.isMaster();
