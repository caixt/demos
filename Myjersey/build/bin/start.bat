cd ..
java -Xms128m -Xmx512m -Duyun-biltdog -Dlogback.configurationFile=./conf/logback.xml -cp ./module/*;./lib/*; com.github.cxt.Myjersey.main.JettyServer