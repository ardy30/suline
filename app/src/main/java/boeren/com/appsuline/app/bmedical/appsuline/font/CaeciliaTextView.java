package boeren.com.appsuline.app.bmedical.appsuline.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CaeciliaTextView extends TextView {
	
	private static Typeface sCaecilia;

	public CaeciliaTextView(Context context) {
		super(context);
		if (isInEditMode()) return; //Won't work in Eclipse graphical layout
		setTypeface();
	}

	public CaeciliaTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		setTypeface();
	}

	public CaeciliaTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		setTypeface();
	}
	
	private void setTypeface() {
		if (sCaecilia == null) {
            sCaecilia = Typeface.createFromAsset(getContext().getAssets(), "fonts/CaeciliaLTStd-Roman.otf");
		}
		setTypeface(sCaecilia);
	}
}
