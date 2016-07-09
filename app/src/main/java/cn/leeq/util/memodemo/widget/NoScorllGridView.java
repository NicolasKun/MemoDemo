package cn.leeq.util.memodemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by LeeQ on 2016-7-7.
 */
public class NoScorllGridView extends GridView {
    public NoScorllGridView(Context context) {
        super(context);
    }

    public NoScorllGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScorllGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE)
            return false;
        return super.onTouchEvent(ev);
    }
}
