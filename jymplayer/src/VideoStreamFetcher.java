import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;


public class VideoStreamFetcher {

private	HashMap<String, String> sourcecodevideourls = new HashMap<String, String>();
private	HttpContext	localContext = null;
private	DefaultHttpClient httpClient = new DefaultHttpClient();
private	HttpResponse response = null;
private	BufferedReader textreader = null;

private	String lineRead = "";
private	String s403VideoURL = "";
private	String videoURL = "";
private HttpGet httpGet = null;
private HttpHost target = null;
public static final String FULLHD = "37";
public static final String HD = "22";
public static final String SD = "18";
public static final String LD = "17";


	public void produceEntity(String argvideoUrl) {
		httpClient = new DefaultHttpClient();
		argvideoUrl = argvideoUrl.replaceFirst("http://","");
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		httpGet = new HttpGet("/"+argvideoUrl.split("/")[1]);
		target = new HttpHost(argvideoUrl.split("/")[0],80,"http");
	
		try {
			
			response = httpClient.execute(target,httpGet,localContext);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		if(entity != null) {
			try {
				if (response.getFirstHeader("Content-Type").getValue().toLowerCase().matches("^text/html(.*)"))
					textreader = new BufferedReader(new InputStreamReader(entity.getContent()));
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}


	}

	public void generateStreamURL() {
		try {
			while(lineRead != null) {

				lineRead = textreader.readLine();
				if(lineRead !=null && lineRead.matches("(.*)generate_204(.*)")) {
					lineRead = lineRead.replaceFirst("img.src = '?", "");					
					lineRead = lineRead.replaceFirst("';", "");							
					lineRead = lineRead.replaceFirst("\\u0026", "&");						
					lineRead = lineRead.replaceAll("\\\\", "");			
					lineRead = lineRead.replaceAll("\\s", "");
					s403VideoURL = lineRead.replaceFirst("generate_204", "videoplayback");
					videoURL = lineRead;
					System.out.println(s403VideoURL);
				}
				if(lineRead !=null && lineRead.matches("(.*)\"url_encoded_fmt_stream_map\":(.*)")) {
					lineRead = lineRead.replaceFirst(".*\"url_encoded_fmt_stream_map\": \"", "").replaceFirst("\".*", "").replace("%25","%").replace("\\u0026", "&").replace("\\", "");
					String[] urlStrings = lineRead.split(",");
					int tmpRes = 0;
					for (String urlString : urlStrings) {
						String[] fmtUrlPair = urlString.split("&itag="); // 2011-08-20 \\|
						//						fmtUrlPair[0] = fmtUrlPair[0].replaceFirst("url=http%3A%2F%2F", "http://"); // 2011-08-20 key-value exchanged
						//						fmtUrlPair[0] = fmtUrlPair[0].replaceAll("%3F","?").replaceAll("%2F", "/").replaceAll("%3D","=").replaceAll("%26", "&");
						//						fmtUrlPair[0] = fmtUrlPair[0].replaceFirst("&quality=.*", "");
						fmtUrlPair[0] = formatURLPair(fmtUrlPair[0]);

						tmpRes = Integer.parseInt(fmtUrlPair[1]);
						if(tmpRes == 22 || tmpRes == 37 || tmpRes == 18 || tmpRes == 17) //better to use strings?
							sourcecodevideourls.put(fmtUrlPair[1], fmtUrlPair[0]); // save that URL

					} // for

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {
		} finally {
			httpClient.getConnectionManager().closeExpiredConnections();	
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public HashMap<String, String> getURLMap() {
		return sourcecodevideourls;
	}
	


	private String formatURLPair(String argUrlPair) {
		String tmpString = argUrlPair;
		tmpString = tmpString.replaceFirst("url=http%3A%2F%2F", "http://");
		tmpString = tmpString.replaceAll("%3F","?");
		tmpString = tmpString.replaceAll("%2F","/");
		tmpString = tmpString.replaceAll("%3D","=");
		tmpString = tmpString.replaceAll("%26","&");
		tmpString = tmpString.replaceAll("%2C",",");
		tmpString = tmpString.replaceAll("u0026","&");
		tmpString = tmpString.replaceFirst("&quality=.*", "");
		return tmpString;
	}

}