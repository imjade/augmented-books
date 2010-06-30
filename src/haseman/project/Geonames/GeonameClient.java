package haseman.project.Geonames;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.geonames.Toponym;
import com.google.zxing.client.android1.Main;

import android.location.Location;
import android.util.Log;
import haseman.project.Geonames.GeonameServer;

public class GeonameClient {

	public static List<Toponym> places = new ArrayList<Toponym>();

	private void adjustAz(Vector v) {
		Enumeration e = v.elements();
		while (e.hasMoreElements()) {
			GeonameObject ven = (GeonameObject) e.nextElement();
			// ven.inclination = (ven.checkins / highestCheckin) * 100;
			// Log.e("inc","Inclination is "+ven.inclination);
		}
	}

	/**
	 * Method to return a vector of GeonameObjects that surronds my current
	 * location
	 * 
	 * @param loc
	 *            --> my current location by GPS
	 * @return
	 */
	public Vector<GeonameObject> getNearPlaces(Location loc,String[]featureCodes) {

		Vector<GeonameObject> v = new Vector<GeonameObject>();
		//try {
			places = new ArrayList<Toponym>();

			GeonameObject nearPlace;// = new GeonameObject(Main.ctx);
			
			try{
				Log.e("GeonameClient", "***** before getting nearby");
				Log.e("GeonameClient", "***** before getting nearby loc.getLatitude() "+loc.getLatitude());
				Log.e("GeonameClient", "***** before getting nearby loc.getLongitude() "+loc.getLongitude());
				Log.e("GeonameClient", "***** before getting nearby featureCodes "+featureCodes);
				
				places = GeonameServer.findNearby(loc.getLatitude(), loc.getLongitude(), null,featureCodes );
				Log.e("GeonameClient", "***** after getting nearby");
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			Log.e("GeonameClient", "***** places found " + places.size() + " *****");
			for (int i = 0; i < Math.min(Main.buttonPlaces.size(), places.size()); i++) {
				
				String featureCode=places.get(i).getFeatureCode();
				Log.e("GeonameClient","Feature code: "+featureCode);
				String place = places.get(i).getName();
				Log.e("GeonameClient","place name: "+place);
				Main.buttonPlaces.get(i).setText(place);
				Log.e("GeonameClient", "***** Place " + i + " " + place + " *****");
				nearPlace = new GeonameObject(Main.ctx);

				nearPlace.location = new Location(place);
				nearPlace.name = place;
				nearPlace.featureCode=featureCode;
				nearPlace.location.setLatitude(places.get(i).getLatitude());
				nearPlace.location.setLongitude(places.get(i).getLongitude());
				nearPlace.inclination = i*1;
				
				v.add(nearPlace);
			}
		//}
		/*catch (Exception e) {
			e.printStackTrace();
			Log.e("GeonameClient", "***** CATCH *****");
		}*/
		return v;
	}
}
