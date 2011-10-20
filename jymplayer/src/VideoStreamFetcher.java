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
	private	DefaultHttpClient httpClient;// = new DefaultHttpClient();
	private	HttpResponse response = null;
	private	BufferedReader textreader = null;

	private	String lineRead;// = "";
	private	String s403VideoURL = "";
	private	String videoURL = "";
	private HttpGet httpGet = null;
	private HttpHost target = null;
	private boolean hasRun = false;


	public void produceEntity(String argvideoUrl) throws IOException {
		if(hasRun) {
			sourcecodevideourls.clear();
		}
		hasRun = true;
		httpClient = new DefaultHttpClient();
		argvideoUrl = argvideoUrl.replaceFirst("http://","");
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		String[] tmp = argvideoUrl.split("/"); //split host = www.youtube.com with extension = /watch?v=GIBQpAsVoWo
		httpGet = new HttpGet("/"+tmp[1]); // add / to replace splitter
		target = new HttpHost(tmp[0],80,"http");
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
			do{
				lineRead = textreader.readLine();
				if(lineRead == null)
					return;
				if(lineRead.matches("(.*)generate_204(.*)")) {
					lineRead = lineRead.replaceFirst("img.src = '?", "");					
					lineRead = lineRead.replaceFirst("';", "");							
					lineRead = lineRead.replaceFirst("\\u0026", "&");						
					lineRead = lineRead.replaceAll("\\\\", "");			
					lineRead = lineRead.replaceAll("\\s", "");
					s403VideoURL = lineRead.replaceFirst("generate_204", "videoplayback");
					videoURL = lineRead;
					//System.out.println("s403"+s403VideoURL);
				}
				if(lineRead.matches("(.*)\"url_encoded_fmt_stream_map\":(.*)")) {
					lineRead = lineRead.replaceFirst(".*\"url_encoded_fmt_stream_map\": \"", "").replaceFirst("\".*", "").replace("%25","%").replace("\\u0026", "&").replace("\\", "");
					String[] urlStrings = lineRead.split(",");
					int tmpRes = 0;
					for (String urlString : urlStrings) {
						String[] fmtUrlPair = urlString.split("&itag=");
						fmtUrlPair[0] = formatURLPair(fmtUrlPair[0]);
						tmpRes = Integer.parseInt(fmtUrlPair[1]);
						if(tmpRes == 22 || tmpRes == 37 || tmpRes == 18 || tmpRes == 17) //better to use strings?
							sourcecodevideourls.put(fmtUrlPair[1], fmtUrlPair[0]); // save that URL
						//System.out.println("fmtUrlPair "+fmtUrlPair[0]);
					} // for
				}
			}while(lineRead != null);
			textreader.close();
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