package ARKit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.widget.LinearLayout;

public class TransparentPanel extends LinearLayout{

	public TransparentPanel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		Paint innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75);
		
		Paint borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);

		RectF drawRect = new RectF();
		drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());

		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);


		this.setVerticalScrollBarEnabled(true);
		this.setHorizontalScrollBarEnabled(true);
		
		
		super.dispatchDraw(canvas);

	}
}