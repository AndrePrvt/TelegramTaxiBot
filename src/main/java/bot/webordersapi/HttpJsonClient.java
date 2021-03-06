package bot.webordersapi;

import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


/**
 * Created by andreyprvt on 13.02.16.
 */
public class HttpJsonClient {

	private static final String APPLICATION_ID = "";
	
	public static String postToURL(String url, String msg ,String autorize, boolean addId) {
		String responseExcptionStr = null;
		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

			HttpPost postRequest = new HttpPost(url);
			StringEntity entity = new StringEntity(msg , Charset.forName("UTF-8"));
			
			postRequest.addHeader("content-type", "application/json; charset=utf-8");
			postRequest.addHeader("Accept-Language", "ru, uk-ua;q=0.8, en;q=0.7");
			if(autorize != null)
				postRequest.addHeader("Authorization", "Basic " + autorize);
			
			if(addId == true)
				postRequest.addHeader("X-WO-API-APP-ID", APPLICATION_ID);
			
			postRequest.setEntity(entity);
			HttpResponse response = client.execute(postRequest);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				responseExcptionStr = EntityUtils.toString(response.getEntity());
				System.out.println(responseExcptionStr);
				return responseExcptionStr;
			}

			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(json);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(responseExcptionStr);
			return responseExcptionStr;
		}
	}
	
	
	public static int putToURL(String url, String msg ,String autorize, boolean addId) {
		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			HttpPut httpPut = new HttpPut(url);
			StringEntity entity = new StringEntity(msg , Charset.forName("UTF-8"));
			httpPut.addHeader("content-type", "application/json; charset=utf-8");
			
			if(autorize != null)
				httpPut.addHeader("Authorization", "Basic " + autorize);
			
			if(addId == true)
				httpPut.addHeader("X-WO-API-APP-ID", APPLICATION_ID);
			
			httpPut.setEntity(entity);
			HttpResponse response = client.execute(httpPut);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {	
				System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
				return 0;
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static String getToURL(String url, String msg, String autorize) {
		try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
			
			HttpGet get = new HttpGet(url);
			get.addHeader("content-type", "application/json");
			
			if(autorize != null)
				get.addHeader("Authorization", "Basic " + autorize);
			
			if(msg != null) {
				StringEntity entity = new StringEntity(msg);
				//set params
			}
			HttpResponse response = client.execute(get);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 || statusCode != 201) {
				System.out.println(EntityUtils.toString(response.getEntity(),
						"UTF-8"));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
