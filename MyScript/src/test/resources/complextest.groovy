import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

println "http-start";

def httpclient = HttpClients.createDefault();
def result = null;

def getRequest = new HttpGet(url);
getRequest.setHeader("Connection", "close");  
getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

def httpResponse = httpclient.execute(getRequest);

def entity = httpResponse.getEntity();
println(httpResponse.getStatusLine().getStatusCode());
if (entity != null) {
	result = EntityUtils.toString(entity, "utf-8");
	output = result;
}
println "http-end";

try{
	def num = 100/0;
}catch(Exception e){
	e.printStackTrace(out);
}

println classLoader;
classLoader.addClasspath("jar/mytest.jar");
println classLoader.loadClass("com.cxt.main.Demo").newInstance();
