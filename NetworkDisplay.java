package com.example.joeym.playground;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class NetworkDisplay extends AppCompatActivity {
    int fontSize = 12;

    private LinearLayout layout;
    public static final Point screenSize = new Point();
    private Vibrator vibrate;
    private View v;
    private Runnable redrawer = new Runnable() {
        @Override
        public void run() {
            v.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_display);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(fontSize);

        //hopefully this returns good values
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);
        int resId = getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resId > 0) {
            screenSize.y -= getResources().getDimensionPixelSize(resId);
        }

        CoordinatorLayout mainLayout = findViewById(R.id.mainlayout);


        layout = findViewById(R.id.layout);
        v = new View(getBaseContext()) {
            final Rect rect720p = new Rect(0, 0, 720, 1280);
            final Rect thisRes = new Rect(0, 0, screenSize.x, screenSize.y);
            Paint defaultPaint = new Paint();

            @Override
            protected void onDraw(Canvas c) {
                super.onDraw(c);
                scheduleDrawable(getBackground(), redrawer, 33
                        /*Math.max (-(System.currentTimeMillis() - lastScreenUpdate - 16), 0)*/);
                //(Math.max (-(System.currentTimeMillis () - time - 16), 0))
                c.drawBitmap(bitmap720p, rect720p, thisRes, defaultPaint);
            }
        };
        mainLayout.addView(v);

        //layout.setOrientation (LinearLayout.HORIZONTAL);
        //layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//
        //CoordinatorLayout.LayoutParams lparams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();

        addButton("button1", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("boop");
            }
        });

        addButton("button2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("doop");
            }
        });


        (new Thread() {
            long time = System.currentTimeMillis();

            @Override
            public void run() {
                while (true) {
                    process();
                    try {
                        Thread.sleep((int) (Math.max(-(System.currentTimeMillis() - time - 16), 0)));
                    } catch (Exception e) {
                        System.out.println("um");
                    }
                    time = System.currentTimeMillis();
                }
            }
        }).start();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
    }

    public void addButton(String text, View.OnClickListener action) {
        Button b = new Button(getBaseContext());
        b.setText(text);
        b.setOnClickListener(action);
        layout.addView(b);
    }

    public static Paint textPaint;

    private Bitmap bitmap720p;

    public void process() {
        Bitmap tempBitmap = Bitmap.createBitmap(720, 1280,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(tempBitmap);
        gameThread:
        {

            drawString("poop", 360, 640, c, Pos.CENTER_CENTER);

            drawString("topleft", 0, 0, c, Pos.TOP_LEFT);
            drawString("topcenter", 360, 0, c, Pos.TOP_CENTER);
            drawString("topright", 720, 0, c, Pos.TOP_RIGHT);

            drawString("middleleft", 0, 640, c, Pos.CENTER_LEFT);
            drawString("middleright", 720, 640, c, Pos.CENTER_RIGHT);

            drawString("bottomleft", 0, 1280, c, Pos.BOTTOM_LEFT);
            drawString("bottomright", 720, 1280, c, Pos.BOTTOM_RIGHT);
        }
        bitmap720p = tempBitmap;
    }

    enum Pos {
        TOP_LEFT, BOTTOM_LEFT, TOP_RIGHT, BOTTOM_RIGHT, TOP_CENTER, BOTTOM_CENTER, CENTER_RIGHT, CENTER_LEFT, CENTER_CENTER
    }

    private void drawString(String text, double x, double y, Canvas c, Pos pos) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        double width = bounds.right - bounds.left;
        double height = bounds.bottom - bounds.top;//well, it works
        switch (pos) {
            case TOP_LEFT:
                y += height;
                break;
            case TOP_RIGHT:
                y += height;
                x -= width;
                break;
            case TOP_CENTER:
                y += height;
                x -= width / 2.0;
                break;
            case BOTTOM_LEFT:
                break;
            case CENTER_LEFT:
                y += height / 2.0;
                break;
            case BOTTOM_RIGHT:
                x -= width;
                break;
            case CENTER_RIGHT:
                y += height / 2.0;
                x -= width;
                break;
            case BOTTOM_CENTER:
                x -= width / 2.0;
                break;
            case CENTER_CENTER:
                y += height / 2.0;
                x -= width / 2.0;
                break;
        }
        c.drawText(text, (float) x, (float) y, textPaint);
        //c.drawText (bounds.left + ", " + bounds.top + ", " + bounds.right + ", " + bounds.bottom, (float)x, (float)y, textPaint);
        //c.drawText (width + ", " + height, (float)x, (float)y + fontSize, textPaint);
    }

    //Thread thread = new Thread (){
    //    long time = System.currentTimeMillis();
    //    @Override
    //    public void run (){
    //        while (true) {
    //            c.invalidate();
    //            try {
    //                Thread.sleep((int)(Math.max (-(System.currentTimeMillis () - time - 16), 0)));
    //            } catch (Exception e) {
    //                System.out.println("um");
    //            }
    //            //System.out.println(System.currentTimeMillis() - time);
    //            time = System.currentTimeMillis();
    //        }
    //    }
    //};


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_network_display, menu);
    //    return true;
    //}

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    // Handle action bar item clicks here. The action bar will
    //    // automatically handle clicks on the Home/Up button, so long
    //    // as you specify a parent activity in AndroidManifest.xml.
    //    int id = item.getItemId();
//
    //    //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
    //        return true;
    //    }
//
    //    return super.onOptionsItemSelected(item);
    //}
}
