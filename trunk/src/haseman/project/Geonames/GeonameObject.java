package haseman.project.Geonames;

import com.google.zxing.client.android1.Main;

import ARKit.ARButton;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;

public class GeonameObject extends ARButton {
	public String name;
	public String featureCode;

	int width = 20;
	int height = 20;
	int x;
	int y;

	public GeonameObject(Context ctx) {
		super(ctx);
		inclination = 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void draw(Canvas c) {

		if (name != null) {
			//p.setColor(Color.WHITE);
			//c.drawText(name, getLeft(), getTop(), p);

			int i = 0;
			while (i < Main.buttonPlaces.size()) {
				if (Main.buttonPlaces.get(i).getText().toString() == name)
					break;
				i++;
			}

			if (i > Main.buttonPlaces.size() - 1) {
				Log.e("GeonameObject",
						"***** DIDN'T FIND BUTTON with same name: " + name);
				return;
			}

			x = getLeft();
			y = getTop();

			AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, x, y);
			Main.linearLayoutPlaces.get(i).setLayoutParams(params);
			Main.linearLayoutPlaces.get(i).setVisibility(View.VISIBLE);
		}
	}
}