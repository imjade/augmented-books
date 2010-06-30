package ARKit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.widget.Button;

public class ARButton extends Button{
	public volatile float azimuth; //Angle from north
	public volatile float distance; //Distance to object
	public volatile float inclination = -1; //angle off horizon.
	public volatile Location location;
	
	public volatile int x;
	public volatile int y;
	public volatile boolean visible = false;
	
	public static Location deviceLocation;
	//used to compute inclination
	public static float currentAltitude = 0;
	protected Paint p = new Paint();
	
	public ARButton(Context ctx)
	{
		super(ctx);
	}
	/*public ARSphericalView(Context ctx, Button b ,Canvas c){
		super(ctx);
		b.draw(c);
	}*/
	public ARButton(Context ctx, Location deviceLocation, Location objectLocation)
	{
		super(ctx);
		if(deviceLocation != null)
		{
			azimuth = deviceLocation.bearingTo(objectLocation);
			distance = deviceLocation.distanceTo(objectLocation);
			if(deviceLocation.hasAccuracy() && objectLocation.hasAltitude())
			{
				double opposite;
				boolean neg = false;
				if(objectLocation.getAltitude() > deviceLocation.getAltitude())
				{
					opposite = objectLocation.getAltitude() - deviceLocation.getAltitude();
				}
				else
				{
					opposite = deviceLocation.getAltitude() - objectLocation.getAltitude();
					neg = true;
				}
				inclination = (float) Math.atan((opposite/distance));
				if(neg)
					inclination = inclination * -1;
			}
		}
	}
	@Override
	public void draw(Canvas c){
	}
}
