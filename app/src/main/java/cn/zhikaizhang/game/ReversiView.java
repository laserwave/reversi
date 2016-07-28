package cn.zhikaizhang.game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

import cn.zhikaizhang.bean.Move;
import cn.zhikaizhang.reversi.R;
import cn.zhikaizhang.util.Util;


/**
 *  棋盘界面
 */
public class ReversiView extends SurfaceView implements Callback {

	private RenderThread thread;

	/**
	 * 屏幕宽度
	 */
	private float screenWidth;

	/**
	 * 本View背景宽高
	 */
	private float bgLength;

	/**
	 * 本View棋盘宽高
	 */
	private float chessBoardLength;

	/**
	 * 背景宽占屏幕宽度的比重的数组
	 */
	private float scale[] = new float[] { 0.75f, 0.80f, 0.85f, 0.90f, 0.95f };

	/**
	 * 背景宽占屏幕宽度的比重的索引
	 */
	private int scaleLevel = 2;

	private static final int M = 8;

	/**
	 * 棋格边长
	 */
	private float a;

	private float chessBoardLeft;
	private float chessBoardRight;
	private float chessBoardTop;
	private float chessBoardBottom;

	private static final byte NULL = Constant.NULL;
	private static final byte BLACK = Constant.BLACK;
	private static final byte WHITE = Constant.WHITE;

	private float margin;

	private byte[][] chessBoard;
	private int[][] index;

	private Bitmap[] images;

	private Bitmap background;

	private float ratio = 0.9f;

	public ReversiView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReversiView);
		ratio = typedArray.getFloat(R.styleable.ReversiView_ratio, 0.9f);


		getHolder().addCallback(this);

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		bgLength = screenWidth * scale[scaleLevel];
		chessBoardLength = 8f / 9f * bgLength;
		a = chessBoardLength / 8;
		margin = 1f / 18f * bgLength;
		chessBoardLeft = margin;
		chessBoardRight = chessBoardLeft + M * a;
		chessBoardTop = margin;
		chessBoardBottom = chessBoardTop + M * a;
		images = new Bitmap[22];
		loadChesses(context);
		background = loadBitmap(bgLength, bgLength, context.getResources().getDrawable(R.drawable.mood));
		initialChessBoard();
	}

	public ReversiView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ReversiView(Context context) {
		this(context, null, 0);
	}

	public void initialChessBoard(){
		chessBoard = new byte[M][M];
		index = new int[M][M];

		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				chessBoard[i][j] = NULL;
			}
		}
		chessBoard[3][3] = WHITE;
		chessBoard[3][4] = BLACK;
		chessBoard[4][3] = BLACK;
		chessBoard[4][4] = WHITE;

		index[3][3] = 11;
		index[3][4] = 0;
		index[4][3] = 0;
		index[4][4] = 11;
	}


	private int updateIndex(int index, int color) {

		if (index == 0 || index == 11) {
			return index;
		} else if (index >= 1 && index <= 10 || index >= 12 && index <= 21) {
			return (index + 1) % 22;
		} else {
			return defaultIndex(color);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) bgLength, View.MeasureSpec.EXACTLY);
		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) bgLength, View.MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void move(byte[][] chessBoard, List<Move> reversed, Move move, int chessColor) {

		Util.copyBinaryArray(chessBoard, this.chessBoard);
		for (int i = 0; i < reversed.size(); i++) {
			int reverseRow = reversed.get(i).row;
			int reverseCol = reversed.get(i).col;
			if (chessBoard[reverseRow][reverseCol] == WHITE) {
				index[reverseRow][reverseCol] = 1;
			} else if (chessBoard[reverseRow][reverseCol] == BLACK) {
				index[reverseRow][reverseCol] = 12;
			}
		}
		int row = move.row, col = move.col;
		if (chessBoard[row][col] == WHITE) {
			index[row][col] = 11;
		} else if (chessBoard[row][col] == BLACK) {
			index[row][col] = 0;
		}

	}

	public void update() {

		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				if (chessBoard[i][j] == NULL)
					continue;
				index[i][j] = updateIndex(index[i][j], chessBoard[i][j]);
			}
		}

	}

	public boolean inChessBoard(float x, float y) {
		return x >= chessBoardLeft && x <= chessBoardRight && y >= chessBoardTop && y <= chessBoardBottom;
	}

	public int getRow(float y) {
		return (int) Math.floor((y - chessBoardTop) / a);
	}

	public int getCol(float x) {
		return (int) Math.floor((x - chessBoardLeft) / a);
	}

	public void render(Canvas canvas) {
		/**
		 * 画背景
		 */
		Paint paint1 = new Paint();

		canvas.drawBitmap(background, 0, 0, paint1);

		/**
		 * 画棋盘边框
		 */
		Paint paint2 = new Paint();
		paint2.setColor(Color.BLACK);
		paint2.setStrokeWidth(3);
		for (int i = 0; i < 9; i++) {
			canvas.drawLine(chessBoardLeft, chessBoardTop + i * a, chessBoardRight, chessBoardTop + i * a, paint2);
			canvas.drawLine(chessBoardLeft + i * a, chessBoardTop, chessBoardLeft + i * a, chessBoardBottom, paint2);
		}

		/**
		 * 画棋子
		 */
		Paint paint3 = new Paint();
		for (int col = 0; col < M; col++) {
			for (int row = 0; row < M; row++) {
				if (chessBoard[row][col] != NULL) {
					canvas.drawBitmap(images[index[row][col]], chessBoardLeft + col * a, chessBoardTop + row * a, paint3);
				}
			}
		}
	}

	public int defaultIndex(int color) {
		if (color == WHITE)
			return 11;
		else if (color == BLACK)
			return 0;
		return -1;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new RenderThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setRunning(false);
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	public Bitmap loadBitmap(float width, float height, Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, (int) width, (int) height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 加载棋子图片
	 */
	private void loadChesses(Context context) {

		images[0] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black1));
		images[1] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black2));
		images[2] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black3));
		images[3] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black4));
		images[4] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black5));
		images[5] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black6));
		images[6] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black7));
		images[7] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black8));
		images[8] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black9));
		images[9] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black10));
		images[10] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.black11));
		images[11] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white1));
		images[12] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white2));
		images[13] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white3));
		images[14] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white4));
		images[15] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white5));
		images[16] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white6));
		images[17] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white7));
		images[18] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white8));
		images[19] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white9));
		images[20] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white10));
		images[21] = loadBitmap(a, a, context.getResources().getDrawable(R.drawable.white11));
	}

}
