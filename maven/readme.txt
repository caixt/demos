maven-model-builder-3.5.4.jar\maven-model-builder-3.5.4.jar\org\apache\maven\model\pom-4.0.0.xml
docker run -p 18081:8081 --name nexus --restart always -d docker.io/sonatype/nexus

D:\java\apache-maven-3.5.2\bin\mvn.cmd package -U -Dmaven.test.skip=true -settings=./settings-x.xml -Dmaven.repo.local=./data2>run.log

mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.5.2 -settings=./settings-x.xml

mvnw package -settings=./settings-x.xml