package boeren.com.appsuline.app.bmedical.appsuline.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import boeren.com.appsuline.app.bmedical.appsuline.R;


public class DinProTextView extends TextView {

	public DinProTextView(Context context) {
		super(context);
		if (isInEditMode()) return;
		parseAttributes(null);
	}

	public DinProTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	public DinProTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}
	
	private void parseAttributes(AttributeSet attrs) {
		int typeface;
		if (attrs == null) { //Not created from xml
			typeface = DinPro.DINPro_REGULAR;
		} else {
		    TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.DinProTextView);
		    typeface = values.getInt(R.styleable.DinProTextView_typeface, DinPro.DINPro_REGULAR);
		    values.recycle();
		}
	    setTypeface(getDinPro(typeface));
	}
	
	public void setRobotoTypeface(int typeface) {
	    setTypeface(getDinPro(typeface));
	}
	
	private Typeface getDinPro(int typeface) {
		return getRoboto(getContext(), typeface);
	}
	
	public static Typeface getRoboto(Context context, int typeface) {
		switch (typeface) {
            case DinPro.DINPro_BLACK:
			if (DinPro.sDinProBlack == null) {
                DinPro.sDinProBlack = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Black.otf");
			}
			return DinPro.sDinProBlack;
		case DinPro.DINPro_BOLD:
			if (DinPro.sDinProBold == null) {
                DinPro.sDinProBold = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Bold.otf");
			}
			return DinPro.sDinProBold;
		case DinPro.DINPro_LIGHT:
			if (DinPro.sDinProLight == null) {
                DinPro.sDinProLight = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Light.otf");
			}
			return DinPro.sDinProLight;
		case DinPro.DINPro_MEDIUM:
			if (DinPro.sDinProMedium == null) {
                DinPro.sDinProMedium = Typeface.createFromAsset(context.getAssets(), "fonts/DINProMedium.otf");
			}
			return DinPro.sDinProMedium;
		default:
		case DinPro.DINPro_REGULAR:
			if (DinPro.sDinProRegular == null) {
                DinPro.sDinProRegular = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Regular.otf");
			}
			return DinPro.sDinProRegular;

		}

	}
	
	public static class DinPro {

		public static final int DINPro_BLACK = 0;
		public static final int DINPro_BOLD = 1;
		public static final int DINPro_LIGHT = 2;
		public static final int DINPro_MEDIUM = 3;
		public static final int DINPro_REGULAR = 4;



		private static Typeface sDinProBlack;
		private static Typeface sDinProBold;
		private static Typeface sDinProLight;
		private static Typeface sDinProMedium;
		private static Typeface sDinProRegular;


	}
}
