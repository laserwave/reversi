package cn.zhikaizhang.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 *  控制界面刷新的线程
 */
public class RenderThread extends Thread {

		private SurfaceHolder surfaceHolder;
		private ReversiView reversiView;
		private boolean running;

		public RenderThread(SurfaceHolder surfaceHolder, ReversiView reversiView){
			this.surfaceHolder = surfaceHolder;
			this.reversiView = reversiView;
		}
		
		@Override
		public void run() {
			Canvas canvas;
			while(running){
				canvas = null;
				long startTime = System.currentTimeMillis();
				this.reversiView.update();
				long endTime = System.currentTimeMillis();
				
				try{
					canvas = this.surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) {
						this.reversiView.render(canvas);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(canvas != null){
						this.surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				try{
					if((endTime - startTime) <= 100){
						sleep(100 - (endTime - startTime));
					}
					
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				
			}
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
}
