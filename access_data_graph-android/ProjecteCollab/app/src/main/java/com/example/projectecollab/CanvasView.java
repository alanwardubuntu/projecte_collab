package com.example.projectecollab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class CanvasView extends View {

    private Context context;
    private Path mPath;
    private Paint mPaint1, mPaint2, mPaint3;
    private List<Double> x, y;
    private String firstDate, lastDate;
    public boolean canDraw = false;

    int minScX, maxScX, minScY, maxScY;
    double xMin, xMax, yMin, yMax;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        mPath = new Path();

        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(Color.BLUE);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeJoin(Paint.Join.ROUND);
        mPaint1.setStrokeWidth(4f);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.BLACK);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeWidth(1f);
        mPaint2.setTextSize(20);

        mPaint3 = new Paint();
        mPaint3.setAntiAlias(true);
        mPaint3.setColor(Color.GRAY);
        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setStrokeJoin(Paint.Join.ROUND);
        mPaint3.setStrokeWidth(1f);
    }

    public void setData (List<Double> xx, List<Double> yy,
                         String firstDateS, String lastDateS) {

        x = xx;
        y = yy;
        firstDate = firstDateS;
        lastDate = lastDateS;
        canDraw = true;
    }

    private int screenx(double x) {

        return new Double((x - xMin)/(xMax - xMin) * (maxScX - minScX) + minScX).intValue();
    }

    private int screeny(double y) {

        return new Double((y - yMin)/(yMax - yMin) * (maxScY - minScY) + minScY).intValue();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        minScX = 10;
        maxScX = getWidth() - 10;
        minScY = getHeight() - 10;
        if (minScY > 0.6*maxScX)
            minScY = 6 * maxScX / 10;
        maxScY = 10;

        if (canDraw) {
            xMin = xMax = x.get(0);
            yMin = yMax = y.get(0);
            for (int i = 1; i < x.size(); i++) {
                if (x.get(i) < xMin) xMin = x.get(i);
                if (x.get(i) > xMax) xMax = x.get(i);
                if (y.get(i) < yMin) yMin = y.get(i);
                if (y.get(i) > yMax) yMax = y.get(i);
            }

            mPath.reset();
            mPath.moveTo(screenx(x.get(0)), screeny(y.get(0)));
            for (int i = 1; i < x.size(); i++)
                mPath.lineTo(screenx(x.get(i)), screeny(y.get(i)));
            canvas.drawPath(mPath, mPaint1);

            canvas.drawText("" + yMin, minScX, minScY - 10, mPaint2);
            canvas.drawText("" + yMax, minScX, maxScY + 10, mPaint2);
            canvas.drawText("dies", (minScX + maxScX) / 2, minScY - 10, mPaint2);

            int threequart = 3 * maxScX / 4;
            int onequart = minScY / 4;
            Log.println(3, "Error", "3: " + threequart + "   1: " + onequart);
            Log.println(3, "Error", "firstdate: " + firstDate + "  lastdate: " + lastDate);
            canvas.drawText("Inici: " + firstDate, threequart, onequart, mPaint2);
            canvas.drawText("Final: " + lastDate, threequart, onequart + minScY / 25, mPaint2);

            mPath.reset();
            double day = 0.0;
            int scx = screenx(day);
            while (scx < maxScX) {
                mPath.moveTo(scx, minScY - 10);
                mPath.lineTo(scx, maxScY + 10);
                day += 1.0;
                scx = screenx(day);
            }
            canvas.drawPath(mPath, mPaint3);

        } else {
            mPath.reset();
            mPath.moveTo(minScX, minScY);
            mPath.lineTo(maxScX, maxScY);
            mPath.moveTo(minScX, maxScY);
            mPath.lineTo(maxScX, minScY);
            canvas.drawPath(mPath, mPaint1);
        }

    }
}
