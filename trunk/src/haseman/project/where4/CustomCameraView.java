package haseman.project.where4;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CustomCameraView extends SurfaceView
{
	Camera camera;
	SurfaceHolder previewHolder;
	boolean previewing;
	
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	public SurfaceHolder getPreviewHolder() {
		return previewHolder;
	}
	public void setPreviewHolder(SurfaceHolder previewHolder) {
		this.previewHolder = previewHolder;
	}
	
	
	//Callback for the surfaceholder
	SurfaceHolder.Callback surfaceHolderListener = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            camera=Camera.open();
            try 
            {
            	camera.setPreviewDisplay(previewHolder);
            	previewing=true;
            }
            catch (Throwable t) {
            }
        }

		public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h)
		{
			Parameters params = camera.getParameters();
			params.setPreviewSize(320, 480);
			params.setPictureFormat(PixelFormat.JPEG);
	        camera.setParameters(params);
	        camera.startPreview();
	        previewing=true;
		}

		public void surfaceDestroyed(SurfaceHolder arg0)
		{
			try{
			camera.stopPreview();
			camera.release();
			previewing=false;
			
		}catch(Exception e){
		Log.e("CustomCameraView","in catch of releasing the camera");
			e.printStackTrace();
		}
    }
	};
	public CustomCameraView(Context ctx)
	{
		super(ctx);
		if(previewHolder ==null){
		previewHolder = this.getHolder();
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        previewHolder.addCallback(surfaceHolderListener);
        setBackgroundColor(Color.TRANSPARENT);
		}
		else{
			if (camera == null) {
			      camera = Camera.open();
			      try {
			    	  camera.setPreviewDisplay(previewHolder);
			      }
			      catch (IOException e) {
			    	  Log.e("****", "catch from Custom camera view");
			      }
			}
		}
	}
	
	public CustomCameraView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	protected void onDraw (Canvas canvas)
	{
		super.onDraw(canvas);
		//CompassListener.sensorMan.get
		//Paint p = new Paint();
		//p.setColor(Color.WHITE);
		//canvas.drawText(String.valueOf(CompassListener.direction), 170, 0, new Paint());
	}
	public void closeCamera()
	{
		if(camera != null)
			{
			camera.release();
			previewing=false;
			previewHolder=null;
			}
	}
	
	public void pauseCamera(){
		if(camera!=null){
			camera.stopPreview();
			camera=null;
			
		}
	}
	public void resumeCamera(){
		if(previewHolder!=null){
			camera=Camera.open();
			camera.startPreview();
		}
	}
	@Override
	public void dispatchDraw(Canvas c)
	{
		super.dispatchDraw(c);
		//Log.i("Drawing","Got Dispatch!");
	}
	
	
	/**
	   * Asks the camera hardware to begin drawing preview frames to the screen.
	   */
	  public void startPreview() {
	    if (camera != null && !previewing) {
	      camera.startPreview();
	      previewing = true;
	    }
	  }

	  /**
	   * Tells the camera to stop drawing preview frames.
	   */
	  public void stopPreview() {
	    if (camera != null ) {
	      
	        camera.setPreviewCallback(null);
	      
	      camera.stopPreview();
	      
	      previewing = false;
	    }
	  }
	  
}
