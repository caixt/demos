import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

println "start";

HttpClient httpclient = HttpClients.createDefault();
String result = null;

HttpGet getRequest = new HttpGet(url);
getRequest.setHeader("Connection", "close");  
getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

HttpResponse httpResponse = httpclient.execute(getRequest);

HttpEntity entity = httpResponse.getEntity();
println(httpResponse.getStatusLine().getStatusCode());
if (entity != null) {
	result = EntityUtils.toString(entity, "utf-8");
	output = result;
}
println "end";