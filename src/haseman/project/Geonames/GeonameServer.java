package haseman.project.Geonames;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.geonames.*;

import android.util.Log;

/**
 * provides static methods to access the <a
 * href="http://www.geonames.org/export/ws-overview.html">GeoNames web
 * services</a>.
 * <p>
 * Note : values for some fields are only returned with sufficient {@link Style}
 * . Accessing these fields (admin codes and admin names, elevation,population)
 * will throw an {@link InsufficientStyleException} if the {@link Style} was not
 * sufficient.
 * 
 * @author marc@geonames
 * 
 */
public class GeonameServer {

	private static Logger logger = Logger.getLogger("org.geonames");

	private static String USER_AGENT = "geonames-webservice-client-1.0.3";

	private static String geoNamesServer = "http://ws.geonames.org";

	private static String geoNamesServerFailover = "http://ws.geonames.org";

	private static long timeOfLastFailureMainServer;

	private static Style defaultStyle = Style.MEDIUM;

	private static int readTimeOut = 120000;

	private static int connectTimeOut = 10000;

	/**
	 * adds the default style to the url. The default style can be set with the
	 * static setter. It is 'MEDIUM' if not set.
	 * 
	 * @param url
	 * @return url with the style parameter appended
	 */
	private static String addDefaultStyle(String url) {
		if (defaultStyle != Style.MEDIUM) {
			url = url + "&style=" + defaultStyle.name();
		}
		return url;
	}

	/**
	 * returns the currently active server. Normally this is the main server, if
	 * the main server recently failed then the failover server is returned. If
	 * the main server is not available we don't want to try with every request
	 * whether it is available again. We switch to the failover server and try
	 * from time to time whether the main server is again accessible.
	 * 
	 * @return
	 */
	private static String getCurrentlyActiveServer() {
		if (timeOfLastFailureMainServer == 0) {
			// no problems with main server
			return geoNamesServer;
		}
		// we had problems with main server
		if (System.currentTimeMillis() - timeOfLastFailureMainServer > 1000l * 60l * 10l) {
			// but is was some time ago and we switch back to the main server to
			// retry. The problem may have been solved in the mean time.
			timeOfLastFailureMainServer = 0;
			return geoNamesServer;
		}
		if (System.currentTimeMillis() < timeOfLastFailureMainServer) {
			throw new Error("time of last failure cannot be in future.");
		}
		// the problems have been very recent and we continue with failover
		// server
		return geoNamesServerFailover;
	}

	/**
	 * opens the connection to the url and sets the user agent. In case of an
	 * IOException it checks whether a failover server is set and connects to
	 * the failover server if it has been defined and if it is different from
	 * the normal server.
	 * 
	 * @param url
	 *            the url to connect to
	 * @return returns the inputstream for the connection
	 * @throws IOException
	 */
	private static InputStream connect(String url) throws IOException {
		String currentlyActiveServer = getCurrentlyActiveServer();
		try {
			url+="&radius=60&maxRows=30";
			
			Log.i("Maram","***** WebService connect *****");
			Log.i("Maram","***** Server "+getCurrentlyActiveServer()+" *****");
			URLConnection conn = new URL(currentlyActiveServer + url).openConnection();
			Log.i("Maram","***** URL "+url+" *****");
			Log.i("Maram","***** opened connection *****");
			conn.setConnectTimeout(connectTimeOut);
			conn.setReadTimeout(readTimeOut);
			//conn.setRequestProperty("User-Agent", USER_AGENT);
			Log.i("Maram","***** before stream *****");
			InputStream in = conn.getInputStream();
			Log.i("Maram","***** after stream *****");
			return in;
		} catch (IOException e) {
			
			Log.i("Maram","***** problems connecting to geonames server " + geoNamesServer + " *****");
			// we cannot reach the server
			logger.log(Level.WARNING, "problems connecting to geonames server "
					+ geoNamesServer, e);
			// is a failover server defined?
			if (geoNamesServerFailover == null
			// is it different from the one we are using?
					|| currentlyActiveServer.equals(geoNamesServerFailover)) {
				throw e;
			}
			timeOfLastFailureMainServer = System.currentTimeMillis();
			Log.i("Maram","***** trying to connect to failover server " + geoNamesServerFailover + " *****");
			logger.info("trying to connect to failover server "
					+ geoNamesServerFailover);
			// try failover server
			URLConnection conn = new URL(geoNamesServerFailover + url)
					.openConnection();
			conn.setRequestProperty("User-Agent", USER_AGENT
					+ " failover from " + geoNamesServer);
			InputStream in = conn.getInputStream();
			return in;
		}
	}

	private static Toponym getToponymFromElement(Element toponymElement) {
		Toponym toponym = new Toponym();

		toponym.setName(toponymElement.getChildText("name"));
		toponym
				.setAlternateNames(toponymElement
						.getChildText("alternateNames"));
		toponym.setLatitude(Double.parseDouble(toponymElement
				.getChildText("lat")));
		toponym.setLongitude(Double.parseDouble(toponymElement
				.getChildText("lng")));

		String geonameId = toponymElement.getChildText("geonameId");
		if (geonameId != null) {
			toponym.setGeoNameId(Integer.parseInt(geonameId));
		}

		toponym.setCountryCode(toponymElement.getChildText("countryCode"));
		toponym.setCountryName(toponymElement.getChildText("countryName"));

		toponym.setFeatureClass(FeatureClass.fromValue(toponymElement
				.getChildText("fcl")));
		toponym.setFeatureCode(toponymElement.getChildText("fcode"));

		toponym.setFeatureClassName(toponymElement.getChildText("fclName"));
		toponym.setFeatureCodeName(toponymElement.getChildText("fCodeName"));

		String population = toponymElement.getChildText("population");
		if (population != null && !"".equals(population)) {
			toponym.setPopulation(Long.parseLong(population));
		}
		String elevation = toponymElement.getChildText("elevation");
		if (elevation != null && !"".equals(elevation)) {
			toponym.setElevation(Integer.parseInt(elevation));
		}

		toponym.setAdminCode1(toponymElement.getChildText("adminCode1"));
		toponym.setAdminName1(toponymElement.getChildText("adminName1"));
		toponym.setAdminCode2(toponymElement.getChildText("adminCode2"));
		toponym.setAdminName2(toponymElement.getChildText("adminName2"));
		toponym.setAdminCode3(toponymElement.getChildText("adminCode3"));
		toponym.setAdminCode4(toponymElement.getChildText("adminCode4"));

		Element timezoneElement = toponymElement.getChild("timezone");
		if (timezoneElement != null) {
			Timezone timezone = new Timezone();
			timezone.setTimezoneId(timezoneElement.getValue());
			timezone.setDstOffset(Double.parseDouble(timezoneElement
					.getAttributeValue("dstOffset")));
			timezone.setGmtOffset(Double.parseDouble(timezoneElement
					.getAttributeValue("gmtOffset")));
			toponym.setTimezone(timezone);
		}
		return toponym;
	}


	public static List<Toponym> findNearby(double latitude, double longitude,
			FeatureClass featureClass, String[] featureCodes)
			throws IOException, Exception {

		Log.i("Maram","***** findNearBy() entered *****");
		
		List<Toponym> places = new ArrayList<Toponym>();

		String url = "/findNearby?";

		url += "&lat=" + latitude;
		url += "&lng=" + longitude;		
		
		if (featureClass != null) {
			url += "&featureClass=" + featureClass;
		}
		if (featureCodes != null && featureCodes.length > 0) {
			for (String featureCode : featureCodes) {
				url += "&featureCode=" + featureCode;
			}
		}
		url = addDefaultStyle(url);
Log.e("from Test",url);
		SAXBuilder parser = new SAXBuilder();
		/*DefaultHandler h = new DefaultHandler() {
			public void startDocument() { Log.i("###","start document"); }
            public void startElement() { Log.i("###","start element");}
            public void endElement() { Log.i("###","end element");}
            public void endDocument() { Log.i("###","end document"); }
		};*/
		Document doc = parser.build(connect(url));

		Element root = doc.getRootElement();
		for (Object obj : root.getChildren("geoname")) {
			Element toponymElement = (Element) obj;
			Toponym toponym = getToponymFromElement(toponymElement);
			places.add(toponym);
		}

		return places;
	}

	private static void checkException(Element root) throws Exception {
		Element message = root.getChild("status");
		if (message != null) {
			throw new Exception(message.getAttributeValue("message"));
		}
	}

	
	/**
	 * @return the geoNamesServer, default is http://ws.geonames.org
	 */
	public static String getGeoNamesServer() {
		return geoNamesServer;
	}

	/**
	 * @return the geoNamesServerFailover
	 */
	public static String getGeoNamesServerFailover() {
		return geoNamesServerFailover;
	}

	/**
	 * sets the server name for the GeoNames server to be used for the requests.
	 * Default is ws.geonames.org
	 * 
	 * @param geoNamesServer
	 *            the geonamesServer to set
	 */
	public static void setGeoNamesServer(String pGeoNamesServer) {
		if (pGeoNamesServer == null) {
			throw new Error();
		}
		pGeoNamesServer = pGeoNamesServer.trim().toLowerCase();
		// add default http protocol if it is missing
		if (!pGeoNamesServer.startsWith("http://")
				&& !pGeoNamesServer.startsWith("https://")) {
			pGeoNamesServer = "http://" + pGeoNamesServer;
		}
		WebService.setGeoNamesServer(pGeoNamesServer);
	}

	/**
	 * sets the default failover server for requests in case the main server is
	 * not accessible. Default is ws.geonames.org<br>
	 * The failover server is only called if it is different from the main
	 * server.<br>
	 * The failover server is used for commercial GeoNames web service users.
	 * 
	 * @param geoNamesServerFailover
	 *            the geoNamesServerFailover to set
	 */
	public static void setGeoNamesServerFailover(String geoNamesServerFailover) {
		if (geoNamesServerFailover != null) {
			geoNamesServerFailover = geoNamesServerFailover.trim()
					.toLowerCase();
			if (!geoNamesServerFailover.startsWith("http://")) {
				geoNamesServerFailover = "http://" + geoNamesServerFailover;
			}
		}
		WebService.setGeoNamesServerFailover(geoNamesServerFailover);
	}

	/**
	 * @return the defaultStyle
	 */
	public static Style getDefaultStyle() {
		return defaultStyle;
	}

	/**
	 * @param defaultStyle
	 *            the defaultStyle to set
	 */
	public static void setDefaultStyle(Style defaultStyle) {
		WebService.setDefaultStyle(defaultStyle);
	}

	/**
	 * @return the readTimeOut
	 */
	public static int getReadTimeOut() {
		return readTimeOut;
	}

	/**
	 * @param readTimeOut
	 *            the readTimeOut to set
	 */
	public static void setReadTimeOut(int readTimeOut) {
		WebService.setReadTimeOut(readTimeOut);
	}

	/**
	 * @return the connectTimeOut
	 */
	public static int getConnectTimeOut() {
		return connectTimeOut;
	}

	/**
	 * @param connectTimeOut
	 *            the connectTimeOut to set
	 */
	public static void setConnectTimeOut(int connectTimeOut) {
		WebService.setConnectTimeOut(connectTimeOut);
	}

}