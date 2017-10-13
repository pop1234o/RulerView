package liyafeng.com.ruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.math.BigDecimal;

/**
 * Created by liyafeng on 2017/10/13.
 */

public class SimpleRulerView extends FrameLayout {

	private float mCurrentNum;
	private float mDUnit = 0.1f;//单位间隔
	private float mMin;
	private float mMax;

	private float mDistance;//间距
	private Paint mNumPaint;

	private String mUnit;
	private Paint mUnitPaint;
	private Rect bounds;
	private int mNumMarginRight;
	private int mNumHeight;
	private int mlineHeight;
	private Paint mVerticalLinePaint;
	private int mNumMarginBottom;


	public SimpleRulerView(Context context) {
		super(context);
		init();
	}


	public SimpleRulerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SimpleRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	public void config(float dUnit, String unit, int color) {
		mDUnit = dUnit;
		mUnit = unit;
		mNumPaint.setColor(color);
		mUnitPaint.setColor(color);
		mVerticalLinePaint.setColor(color);
		invalidate();
	}

	/**
	 * 范围
	 *
	 * @param min
	 * @param max
	 */
	public void setRange(float min, float max) {
		mMin = round(min);
		mMax = round(max);
		mCurrentNum = mMin;
	}

	/**
	 * 当前值
	 *
	 * @param num
	 */
	public void scrollTo(float num) {
		mCurrentNum = round(num);
		check();
		InnerRuler ruler = (InnerRuler) getChildAt(0);
		ruler.innerScrollTo(num);

	}

	private void check() {
		if (mCurrentNum > mMax) {
			mCurrentNum = mMax;
		}
		if (mCurrentNum < mMin) {
			mCurrentNum = mMin;
		}
	}

	public void smoothScrollTo(float num) {
		mCurrentNum = round(num);
		check();
		InnerRuler ruler = (InnerRuler) getChildAt(0);
		ruler.smoothScrollTo(num);
	}

	private void init() {

		setWillNotDraw(false);
		int color = Color.parseColor("#1aa260");
		mNumPaint = new Paint();
		mNumPaint.setTextSize(dp2px(43));
		mUnitPaint = new Paint();
		mUnitPaint.setTextSize(dp2px(15));
		mUnitPaint.setTypeface(Typeface.SERIF);
		mNumPaint.setTypeface(Typeface.SERIF);
		mNumPaint.setColor(color);
		mUnitPaint.setColor(color);
		mVerticalLinePaint = new Paint();
		mVerticalLinePaint.setColor(color);
		mVerticalLinePaint.setStrokeWidth(dp2px(5));


		bounds = new Rect();
		mNumMarginRight = dp2px(10);
		mNumMarginBottom = dp2px(20);


		mUnit = "kg";
		mDistance = dp2px(8);
		mlineHeight = dp2px(45);
		mMin = 31.0f;
		mMax = 115.5f;

		mCurrentNum = mMin;

		mNumPaint.getTextBounds("0", 0, 1, bounds);
		mNumHeight = bounds.height();

		InnerRuler innerRuler = new InnerRuler(getContext());
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.topMargin = (int) (mNumPaint.getTextSize() + mNumMarginBottom);
		addView(innerRuler);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawNum(canvas);
		int heightStart = mNumHeight + mNumMarginBottom;
		int canvasHalfWidth = canvas.getWidth() / 2;
		canvas.drawLine(canvasHalfWidth, heightStart, canvasHalfWidth, heightStart + mlineHeight, mVerticalLinePaint);

	}

	private void drawNum(Canvas canvas) {

		String num = String.valueOf(mCurrentNum);

		mNumPaint.getTextBounds(num, 0, num.length(), bounds);
		mNumHeight = bounds.height();

		int halfWidth = bounds.width() / 2;

		canvas.drawText(num, canvas.getWidth() / 2 - halfWidth, bounds.height(), mNumPaint);


		mUnitPaint.getTextBounds(mUnit, 0, mUnit.length(), bounds);
		canvas.drawText(mUnit, canvas.getWidth() / 2 + halfWidth + mNumMarginRight, bounds.height(), mUnitPaint);


	}


	private class InnerRuler extends View {

		private Paint mLinePaint;
		private float mLastX;
		private VelocityTracker tracker;
		private Scroller scroller;
		private int lineNum;
		private int shortLine;
		private Paint mTextPaint;
		private int mTextMargin;


		public InnerRuler(Context context) {
			super(context);
			initRuler();
		}

		public InnerRuler(Context context, AttributeSet attrs) {
			super(context, attrs);
			initRuler();
		}


		public InnerRuler(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			initRuler();
		}

		private void initRuler() {
			mLinePaint = new Paint();
			mLinePaint.setColor(Color.LTGRAY);
			mLinePaint.setStrokeWidth(dp2px(2));
			mTextPaint = new Paint();
			mTextPaint.setColor(Color.parseColor("#88000000"));
			mTextPaint.setTextSize(dp2px(16));

			mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
			shortLine = dp2px(22);
			scroller = new Scroller(getContext());

			mTextMargin = dp2px(8);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			int heightStart = mNumHeight + mNumMarginBottom;

			int canvasWidth = canvas.getWidth();
			int canvasHalfWidth = canvasWidth / 2;

			lineNum = (int) ((mMax - mMin) * 10 + 1);
			//横线

			float strokeWidth = mLinePaint.getStrokeWidth() / 2;
			canvas.drawLine(canvasHalfWidth - strokeWidth, heightStart, canvasHalfWidth + (lineNum - 1) * mDistance + strokeWidth, heightStart, mLinePaint);


			for (int i = 0; i < lineNum; i++) {
				float v = mMin + i * mDUnit;

				float startX = canvasHalfWidth + i * mDistance;
				if (v % 0.5 == 0) {
					canvas.drawLine(startX, heightStart, startX, heightStart + mlineHeight, mLinePaint);

					if (v % 1 == 0) {
						String text = String.valueOf((int) v);
						mTextPaint.getTextBounds(text, 0, text.length(), bounds);
						canvas.drawText(text, startX - bounds.width() / 2, heightStart + mlineHeight + bounds.height() + mTextMargin, mTextPaint);
					}
				} else {
					canvas.drawLine(startX, heightStart, startX, heightStart + shortLine, mLinePaint);
				}
			}

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			if (tracker == null) {
				tracker = VelocityTracker.obtain();
			}
			tracker.addMovement(event);
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastX = event.getRawX();
					if (!scroller.isFinished()) {
						scroller.abortAnimation();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					float rawX = event.getRawX();
					float dx = rawX - mLastX;
					int scrollX = getScrollX();
					//边界值判断
					if (scrollX - dx < 0) {
						dx = scrollX;
					}
					if (scrollX - dx > (mDistance * (lineNum - 1))) {
						dx = scrollX - (mDistance * (lineNum - 1));
					}
					scrollBy((int) -dx, 0);
					mLastX = rawX;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					tracker.computeCurrentVelocity(1000);
					int xVelocity = (int) tracker.getXVelocity();
					scroller.fling(getScrollX(), 0, -xVelocity * 2, 0, 0, (int) ((lineNum - 1) * mDistance), 0, 0);
					invalidate();
					tracker.clear();
					break;
			}
			return true;

		}

		@Override
		public void computeScroll() {
			if (scroller.computeScrollOffset()) {
				int currX = scroller.getCurrX();

				if (scroller.isFinished()) {
					float d = currX % mDistance;
					scrollTo((int) (currX - d), 0);
				} else {
					scrollTo(currX, 0);
					invalidate();
				}
			}
			refreshNum(getScrollX());
		}


		public void smoothScrollTo(float num) {
			float v = (num - mMin) / mDUnit * mDistance;
			scroller.startScroll(getScrollX(), 0, (int) (v - getScrollX()), 0);
			invalidate();
		}

		public void innerScrollTo(float num) {
			float v = (num - mMin) / mDUnit * mDistance;
			scrollTo((int) v, 0);
		}
	}

	private void refreshNum(int scrollX) {

		float v = scrollX / mDistance * mDUnit;
		mCurrentNum = round(mMin + v);
		invalidate();
	}

	public int dp2px(int dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5f);
	}

	public float round(float f) {
		BigDecimal b = new BigDecimal(f);
		return b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

	}

}
