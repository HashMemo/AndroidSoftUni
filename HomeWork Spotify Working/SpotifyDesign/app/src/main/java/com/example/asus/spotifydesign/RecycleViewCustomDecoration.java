package com.example.asus.spotifydesign;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Asus on 5.9.2016 г..
 */

public class RecycleViewCustomDecoration extends RecyclerView.ItemDecoration {

    Paint bluePaint, redPaint, blackPaint;
    int offset;

    public RecycleViewCustomDecoration(Context context) {
        offset = 10;
        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        redPaint.setColor(Color.RED);
        bluePaint.setColor(Color.BLUE);
        blackPaint.setColor(ContextCompat.getColor(context,R.color.colorSpotifyBackground));

        redPaint.setStyle(Paint.Style.FILL);
        bluePaint.setStyle(Paint.Style.FILL);
        blackPaint.setStyle(Paint.Style.FILL);

        redPaint.setStrokeWidth(1f);
        bluePaint.setStrokeWidth(1f);
        blackPaint.setStrokeWidth(1f);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(offset, offset, offset, offset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            c.drawRect(
                    layoutManager.getDecoratedLeft(child),
                    layoutManager.getDecoratedTop(child),
                    layoutManager.getDecoratedRight(child),
                    layoutManager.getDecoratedBottom(child),
                    blackPaint);
            c.drawRect(
                    layoutManager.getDecoratedLeft(child) + offset,
                    layoutManager.getDecoratedTop(child) + offset,
                    layoutManager.getDecoratedRight(child) - offset,
                    layoutManager.getDecoratedBottom(child) - offset,
                    blackPaint);

        }

    }
}
