/**
 * CopyRight(C) www.bugull.com
 */
package com.android.leo.dialview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by liujian (xiaojianmailbox@gmail.com) on 2016/6/7.
 */
public class DialView extends View {

	/**
	 * Default values
	 */
	private static final int DEFAULT_BORDER_COLOR = Color.BLUE;
	private static final int DEFAULT_FINGER_COLOR = Color.BLUE;
	private static final int DEFAULT_BORDER_PADDING = 8;
	private static final int DEFAULT_OUTER_BORDER_WIDTH = 2;
	private static final int DEFAULT_MEDIUM_BORDER_WIDTH = 16;
	private static final int DEFAULT_INNER_BORDER_WIDTH = 20;
	private static final int DEFAULT_START_ANGLE = 135;
	private static final int DEFAULT_END_ANGLE = 405;
	private static final float DEFAULT_PROGRESS_VALUE = 0f;


	private Paint mBorderPaint;
	private Paint mFingerPaint;
	/**
	 * The color of the dial's border
	 */
	private int mBorderColor;

	/**
	 * The color of the dial's finger
	 */
	private int mFingerColor;

	/**
	 * The dial's outer border width
	 */
	private float mOuterBorderWidth;

	/**
	 * The dial's medium border width
	 */
	private float mMediumBorderWidth;

	/**
	 * The dial's inner border width
	 */
	private float mInnerBorderWidth;

	/**
	 * Padding between three Borders
	 */
	private float mBorderPadding;

	/**
	 * The start angle of the dial's boad
	 */
	private int mStartAngle;

	/**
	 * The end angle of the dial's boad
	 */
	private int mEndAngle;

	/**
	 * The dial's finger progress -- 0-1
	 */
	private float mProgress;


	public DialView(Context context) {
		this(context, null);
	}

	public DialView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}


	private void init(Context context, AttributeSet attrs, int defStyleAttr) {

		mBorderPaint = new Paint();
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setStyle(Paint.Style.STROKE);

		mFingerPaint = new Paint();
		mFingerPaint.setAntiAlias(true);
		mFingerPaint.setStrokeWidth(1f);
		mFingerPaint.setStyle(Paint.Style.FILL);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DialView, defStyleAttr, 0);

		try {
			mBorderColor = attributes.getColor(R.styleable.DialView_dv_borderColor, DEFAULT_BORDER_COLOR);
			mFingerColor = attributes.getColor(R.styleable.DialView_dv_fingerColor, DEFAULT_FINGER_COLOR);
			mBorderPadding = attributes.getDimensionPixelSize(R.styleable.DialView_dv_boaderPadding, DEFAULT_BORDER_PADDING);
			mInnerBorderWidth = attributes.getDimensionPixelSize(R.styleable.DialView_dv_innerBoaderWidth, DEFAULT_INNER_BORDER_WIDTH);
			mMediumBorderWidth = attributes.getDimensionPixelSize(R.styleable.DialView_dv_mediumBoaderWidth, DEFAULT_MEDIUM_BORDER_WIDTH);
			mOuterBorderWidth = attributes.getDimensionPixelSize(R.styleable.DialView_dv_outerBoaderWidth, DEFAULT_OUTER_BORDER_WIDTH);
			mStartAngle = attributes.getDimensionPixelSize(R.styleable.DialView_dv_startAngle, DEFAULT_START_ANGLE);
			mEndAngle = attributes.getDimensionPixelSize(R.styleable.DialView_dv_endAngle, DEFAULT_END_ANGLE);
			mProgress = attributes.getFloat(R.styleable.DialView_dv_progress, DEFAULT_PROGRESS_VALUE);
			mFingerPaint.setColor(mFingerColor);
			mBorderPaint.setColor(mBorderColor);
		} finally {
			attributes.recycle();
		}
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mOuterBorderWidth);
		int width = getWidth();
		int height = getHeight();
		//Get the outermost layer radius of the dial
		int radius = width >= height ? height / 2 : width / 2;
		float outerDelta = radius - mOuterBorderWidth / 2;//outermost layer radius
		RectF rectF = new RectF(width / 2 - outerDelta, height / 2 - outerDelta, width / 2 + outerDelta, height / 2 + outerDelta);
		canvas.drawArc(rectF, mStartAngle, mEndAngle - mStartAngle, false, mBorderPaint);//draw outermost layer boader

		mBorderPaint.setStrokeWidth(mMediumBorderWidth);
		float mediumDelta = radius - mOuterBorderWidth / 2 - mBorderPadding - mMediumBorderWidth / 2;//medium layer radius
		rectF = new RectF(width / 2 - mediumDelta, height / 2 - mediumDelta,
				width / 2 + mediumDelta, height / 2 + mediumDelta);
		canvas.drawArc(rectF, mStartAngle, mEndAngle - mStartAngle, false, mBorderPaint);//draw medium layer boader

		canvas.save();
		canvas.translate(width / 2, height / 2);
		mBorderPaint.setStrokeWidth(1f);
		float lineStart = radius - mOuterBorderWidth - mBorderPadding * 2 - mMediumBorderWidth - mInnerBorderWidth;
		float lineEnd = lineStart + mInnerBorderWidth;
		canvas.rotate(mStartAngle);
		//draw inner layer border
		for (int i = 0; i <= (mEndAngle - mStartAngle) / 2; i++) {
			canvas.drawLine(lineStart, 0, lineEnd, 0, mBorderPaint);
			canvas.rotate(2);
		}
		canvas.restore();

		canvas.save();
		canvas.translate(width / 2, height / 2);
		//draw dial's finger
		float fingerLength = radius - mOuterBorderWidth - mBorderPadding * 2 - mMediumBorderWidth - mInnerBorderWidth - mBorderPadding * 2;
		float fingerWidth = fingerLength / 15;
		float fingerAnger = (mEndAngle - mStartAngle) * mProgress + mStartAngle;
		Path fingerPath = generateFingerPath(fingerLength, fingerWidth, fingerAnger);
		canvas.drawPath(fingerPath, mFingerPaint);
		canvas.drawCircle(0, 0, fingerWidth, mFingerPaint);
		canvas.restore();
	}


	/**
	 * Get the dial's finger path
	 * @param fingerLength  finger's langth
	 * @param fingerWidth   finger's width
	 * @param fingerAnger   finger's anger
	 * @return
	 */
	private Path generateFingerPath(float fingerLength, float fingerWidth, float fingerAnger) {
		Path fingerPath = new Path();
		float xCos = (float) Math.cos(Math.PI / 180 * (fingerAnger + 90));
		float ySin = (float) Math.sin(Math.PI / 180 * (fingerAnger + 90));
		float xCos2 = (float) Math.cos(Math.PI / 180 * (fingerAnger - 90));
		float ySin2 = (float) Math.sin(Math.PI / 180 * (fingerAnger - 90));
		fingerPath.lineTo(fingerWidth / 2 * xCos, fingerWidth / 2 * ySin);
		fingerPath.lineTo(fingerWidth / 2 * xCos2, fingerWidth / 2 * ySin2);
		float moveX = fingerLength * (float) Math.cos(Math.PI / 180 * fingerAnger);
		float moveY = fingerLength * (float) Math.sin(Math.PI / 180 * fingerAnger);
		fingerPath.lineTo(0.4f * fingerWidth / 2 * xCos2 + moveX, 0.4f * fingerWidth / 2 * ySin2 + moveY);
		fingerPath.lineTo(0.4f * fingerWidth / 2 * xCos + moveX, 0.4f * fingerWidth / 2 * ySin + moveY);
		fingerPath.lineTo(fingerWidth / 2 * xCos, fingerWidth / 2 * ySin);
		fingerPath.close();
		return fingerPath;
	}


	/**
	 * Set the progress with a animation
	 * @param progress
	 */
	public void setProgressWithAnimation(float progress) {
		if (progress != mProgress) {
			float preProgress = mProgress;
			ObjectAnimator fingerAnim = ObjectAnimator.ofFloat(this, "progress", preProgress, progress);
			fingerAnim.setDuration(1000);
			fingerAnim.setInterpolator(new LinearInterpolator());
			fingerAnim.start();
		}
	}


	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		mBorderColor = borderColor;
	}

	public float getBorderPadding() {
		return mBorderPadding;
	}

	public void setBorderPadding(float borderPadding) {
		mBorderPadding = borderPadding;
	}

	public int getEndAngle() {
		return mEndAngle;
	}

	public void setEndAngle(int endAngle) {
		mEndAngle = endAngle;
	}

	public int getFingerColor() {
		return mFingerColor;
	}

	public void setFingerColor(int fingerColor) {
		mFingerColor = fingerColor;
	}

	public float getInnerBorderWidth() {
		return mInnerBorderWidth;
	}

	public void setInnerBorderWidth(float innerBorderWidth) {
		mInnerBorderWidth = innerBorderWidth;
	}

	public float getMediumBorderWidth() {
		return mMediumBorderWidth;
	}

	public void setMediumBorderWidth(float mediumBorderWidth) {
		mMediumBorderWidth = mediumBorderWidth;
	}

	public float getOuterBorderWidth() {
		return mOuterBorderWidth;
	}

	public void setOuterBorderWidth(float outerBorderWidth) {
		mOuterBorderWidth = outerBorderWidth;
	}

	public float getProgress() {
		return mProgress;
	}

	public void setProgress(float progress) {
		if (progress != mProgress) {
			mProgress = progress;
			invalidate();
		}
	}

	public int getStartAngle() {
		return mStartAngle;
	}

	public void setStartAngle(int startAngle) {
		mStartAngle = startAngle;
	}

	static class SavedState extends BaseSavedState{
		private float progress;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
		}


		@Override
		public String toString() {
			return "DialView.SavedState{"
					+ Integer.toHexString(System.identityHashCode(this))
					+ " finger progress = " + progress + "}";
		}

		public static final Parcelable.Creator<SavedState> CREATOR =
				new Parcelable.Creator<SavedState>(){

					@Override
					public SavedState createFromParcel(Parcel source) {
						return new SavedState(source);
					}

					@Override
					public SavedState[] newArray(int size) {
						return new SavedState[size];
					}
				};
	}


	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState =  super.onSaveInstanceState();

		SavedState savedState = new SavedState(superState);
		savedState.progress = getProgress();
		return savedState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState)state;

		super.onRestoreInstanceState(savedState.getSuperState());
		mProgress = savedState.progress;
		invalidate();
	}
}
