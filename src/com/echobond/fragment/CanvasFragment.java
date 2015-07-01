package com.echobond.fragment;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
/**
 * 
 * @author aohuijun
 *
 */
public class CanvasFragment extends Fragment {
	
	private Bitmap baseBitmap;
	private Canvas canvas;
	private Paint paint;
	private Paint rubber;
	
	private ImageView drawBoard;
	private ImageView penView, backgroundView, rubberView, clearView;
	private ImageView color0, color1, color2, color3, color4, color5, color6, color7, color8, color9, colorA, colorB;
	private SeekBar penSeekBar, rubberSeekBar;
	
	private int selectedMode = 0;
	private String[] colors;
	
	private final static int CHOOSE_PEN = 0;
	private final static int CHOOSE_BACK = 1;
	private final static int CHOOSE_RUBBER = 2;
	private final static int CHOOSE_CLEAR = 3;
	
	private final static int INIT_PEN_SIZE = 5;
	private final static int INIT_RUBBER_SIZE = 30;
	
	private final static int WHITE = 0;
	private final static int DARK_RED = 1;
	private final static int BRIGHT_RED = 2;
	private final static int PINK = 3;
	private final static int YELLOW = 4;
	private final static int ORANGE = 5;
	private final static int BLACK = 6;
	private final static int CYAN = 7;
	private final static int MINT = 8;
	private final static int PURPLE = 9;
	private final static int HORIZON = 10;
	private final static int GREEN = 11;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View canvasView = inflater.inflate(R.layout.fragment_canvas, container, false);
		
    	drawBoard = (ImageView)canvasView.findViewById(R.id.canvas_draw);

		penView = (ImageView)canvasView.findViewById(R.id.canvas_pen);
		backgroundView = (ImageView)canvasView.findViewById(R.id.canvas_background);
		rubberView = (ImageView)canvasView.findViewById(R.id.canvas_eraser);
		clearView = (ImageView)canvasView.findViewById(R.id.canvas_clear);
		penSeekBar = (SeekBar)canvasView.findViewById(R.id.canvas_pen_seekbar);
		rubberSeekBar = (SeekBar)canvasView.findViewById(R.id.canvas_rubber_seekbar);
		
		penView.setImageDrawable(getResources().getDrawable(R.drawable.pentool_selected));

		penView.setOnClickListener(new ModeSelectedListener(CHOOSE_PEN));
		backgroundView.setOnClickListener(new ModeSelectedListener(CHOOSE_BACK));
		rubberView.setOnClickListener(new ModeSelectedListener(CHOOSE_RUBBER));
		clearView.setOnClickListener(new ModeSelectedListener(CHOOSE_CLEAR));
		penSeekBar.setOnSeekBarChangeListener(new StyleChangeListener(CHOOSE_PEN));
		rubberSeekBar.setOnSeekBarChangeListener(new StyleChangeListener(CHOOSE_RUBBER));
		
		colors = getResources().getStringArray(R.array.color_array);

		color0 = (ImageView)canvasView.findViewById(R.id.change_color0);
		color1 = (ImageView)canvasView.findViewById(R.id.change_color1);
		color2 = (ImageView)canvasView.findViewById(R.id.change_color2);
		color3 = (ImageView)canvasView.findViewById(R.id.change_color3);
		color4 = (ImageView)canvasView.findViewById(R.id.change_color4);
		color5 = (ImageView)canvasView.findViewById(R.id.change_color5);
		color6 = (ImageView)canvasView.findViewById(R.id.change_color6);
		color7 = (ImageView)canvasView.findViewById(R.id.change_color7);
		color8 = (ImageView)canvasView.findViewById(R.id.change_color8);
		color9 = (ImageView)canvasView.findViewById(R.id.change_color9);
		colorA = (ImageView)canvasView.findViewById(R.id.change_colora);
		colorB = (ImageView)canvasView.findViewById(R.id.change_colorb);
		
		color0.setOnClickListener(new ColorChangeListener(WHITE));
		color1.setOnClickListener(new ColorChangeListener(DARK_RED));
		color2.setOnClickListener(new ColorChangeListener(BRIGHT_RED));
		color3.setOnClickListener(new ColorChangeListener(PINK));
		color4.setOnClickListener(new ColorChangeListener(YELLOW));
		color5.setOnClickListener(new ColorChangeListener(ORANGE));
		color6.setOnClickListener(new ColorChangeListener(BLACK));
		color7.setOnClickListener(new ColorChangeListener(CYAN));
		color8.setOnClickListener(new ColorChangeListener(MINT));
		color9.setOnClickListener(new ColorChangeListener(PURPLE));
		colorA.setOnClickListener(new ColorChangeListener(HORIZON));
		colorB.setOnClickListener(new ColorChangeListener(GREEN));
		
		initCanvas();
		
		return canvasView;
	}
	
	private void initCanvas() {
		baseBitmap = Bitmap.createBitmap(1080, 764, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(baseBitmap);
		canvas.drawColor(Color.TRANSPARENT);

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(INIT_PEN_SIZE);
		paint.setAntiAlias(true);
		paint.setDither(true);
		
		rubber = new Paint();
		rubber.setAlpha(0);
		rubber.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		rubber.setStrokeWidth(INIT_RUBBER_SIZE);
		rubber.setAntiAlias(true);
		rubber.setDither(true);
		
		canvas.drawBitmap(baseBitmap, new Matrix(), paint);
		drawBoard.setImageBitmap(baseBitmap);
		drawBoard.setOnTouchListener(new View.OnTouchListener() {
			
			float startX;
			float startY;
			
			@SuppressLint("ClickableViewAccessibility") 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					float stopX = event.getX();
					float stopY = event.getY();
					if (selectedMode == CHOOSE_PEN) {
						canvas.drawLine(startX, startY, stopX, stopY, paint);
					} else if (selectedMode == CHOOSE_RUBBER) {
						canvas.drawLine(startX, startY, stopX, stopY, rubber);
					}
					startX = event.getX();
					startY = event.getY();
					drawBoard.setImageBitmap(baseBitmap);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	public class ModeSelectedListener implements OnClickListener {

		private int mode;
		public ModeSelectedListener(int mode) {
			this.mode = mode;
		}

		@Override
		public void onClick(View v) {
			switch (mode) {
			case CHOOSE_PEN:
				penView.setImageDrawable(getResources().getDrawable(R.drawable.pentool_selected));
				backgroundView.setImageDrawable(getResources().getDrawable(R.drawable.change_background_color_nomal));
				rubberView.setImageDrawable(getResources().getDrawable(R.drawable.rubbertool_normal));
				selectedMode = mode;
				break;
			case CHOOSE_BACK:
				penView.setImageDrawable(getResources().getDrawable(R.drawable.pentool_normal));
				backgroundView.setImageDrawable(getResources().getDrawable(R.drawable.change_background_color_selected));
				rubberView.setImageDrawable(getResources().getDrawable(R.drawable.rubbertool_normal));
				selectedMode = mode;
				break;
			case CHOOSE_RUBBER:
				penView.setImageDrawable(getResources().getDrawable(R.drawable.pentool_normal));
				backgroundView.setImageDrawable(getResources().getDrawable(R.drawable.change_background_color_nomal));
				rubberView.setImageDrawable(getResources().getDrawable(R.drawable.rubbertool_selected));
				selectedMode = mode;
				break;
			case CHOOSE_CLEAR:
				if (baseBitmap != null) {
					baseBitmap = Bitmap.createBitmap(1080, 869, Bitmap.Config.ARGB_8888);
					canvas = new Canvas(baseBitmap);
					canvas.drawColor(Color.TRANSPARENT);
					drawBoard.setImageBitmap(baseBitmap);
				}
				paint.setColor(Color.BLACK);
				drawBoard.setBackgroundColor(Color.WHITE);
				paint.setStrokeWidth(INIT_PEN_SIZE);
				rubber.setStrokeWidth(INIT_RUBBER_SIZE);
				penSeekBar.setProgress(INIT_PEN_SIZE);
				rubberSeekBar.setProgress(INIT_RUBBER_SIZE);
				break;
			default:
				break;
			}
		}
		
	}

	public class StyleChangeListener implements OnSeekBarChangeListener {

		private int mode;
		public StyleChangeListener(int mode) {
			this.mode = mode;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			switch (mode) {
			case CHOOSE_PEN:
				paint.setStrokeWidth(progress);
				break;
			case CHOOSE_RUBBER:
				rubber.setStrokeWidth(progress);
				break;
			default:
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public class ColorChangeListener implements OnClickListener {

		private int colorIndex;
		public ColorChangeListener(int index) {
			colorIndex = index;
		}

		@Override
		public void onClick(View v) {
			switch (selectedMode) {
			case CHOOSE_PEN:
				paint.setColor(Color.parseColor(colors[colorIndex]));
				break;
			case CHOOSE_BACK:
				drawBoard.setBackgroundColor(Color.parseColor(colors[colorIndex]));
				break;
			default:
				break;
			}
		}
		
	}

	public ImageView getDrawBoard() {
		return drawBoard;
	}
	
}
