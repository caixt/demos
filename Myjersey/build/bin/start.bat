cd ..
java -Xms512m -Xmx1024m -Duyun-biltdog -Dlogback.configurationFile=./conf/logback.xml -cp ./module/*;./lib/*; uyun.boltdog.api.rest.Start