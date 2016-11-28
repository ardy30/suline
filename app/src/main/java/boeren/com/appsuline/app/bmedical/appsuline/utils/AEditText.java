package boeren.com.appsuline.app.bmedical.appsuline.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Jamil on 14-9-2015.
 */
public class AEditText extends EditText {
    private AEditTextListener mOnImeBack;
    private boolean isNewPlanPage;

    public interface AEditTextListener {
        public void onImeBack(AEditText ctrl, String text);
    }

    /**
     * Construct a LinedEditText object.
     *
     * @param context
     *            Context
     * @param attrs
     *            AttributeSet
     */
    public AEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        TextWatcher watcher = new TextWatcher() {
            private String text;
            private int beforeCursorPosition = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text = s.toString();
                beforeCursorPosition = start;
            }

            @Override
            public void afterTextChanged(Editable s) {
                removeTextChangedListener(this);

                if (isNewPlanPage) {
                    String newText = s.toString();
                    int dotPos = newText.indexOf('.');
                    if (dotPos != -1 && dotPos < newText.length() - 2) {
                        setText(text);
                        setSelection(beforeCursorPosition);
                    }
                }

                addTextChangedListener(this);
            }
        };

        this.addTextChangedListener(watcher);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this, this.getText().toString());
        }

        return super.dispatchKeyEvent(event);
    }


	/*@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

		if (!focused) {
			GuiUtils.setKeypadVisibility(getContext(), this, View.INVISIBLE);
		}
	}*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this, this.getText().toString());
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setOnEditTextImeBackListener(AEditTextListener listener) {
        mOnImeBack = listener;
    }

    public void setIsNewPlanPage(boolean isNewPlanPage) {
        this.isNewPlanPage = isNewPlanPage;
    }
}
