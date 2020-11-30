package com.szymonstasik.kalkulatorsredniejwazonej.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.szymonstasik.kalkulatorsredniejwazonej.R;

public class CurvedBgView extends RelativeLayout {

    public CurvedBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Path path;
    private Paint paint;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getContext().getResources().getColor(R.color.bg_start));
        path = new Path();

        float horizontalOffset = w * .8f;
        float top = -h * .8f;
        float bottom = h;

        RectF ovalRect = new RectF(-horizontalOffset, top, w + horizontalOffset, bottom);
        path.lineTo(ovalRect.left, top);
        path.arcTo(ovalRect, 0, 180, false);
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (path != null)
            canvas.drawPath(path, paint);

        super.onDraw(canvas);
    }
}