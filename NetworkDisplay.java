package com.example.joeym.playground;

import android.content.pm.ActivityInfo;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class NetworkDisplay extends AppCompatActivity {
    //changeables
    int neuronRadius = 30;
    int fontSize = 16;
    static int maxErrorRecording = 1000;

    //useables
    public static final Point screenSize = new Point();

    private Network n = new Network();

    double notifbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_display);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);
        int resId = getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        notifbar = getResources().getDimensionPixelSize(resId);
        ;
        if (resId > 0) {
            screenSize.y -= notifbar;
        }


        buttonslayout = findViewById(R.id.layout);
        CoordinatorLayout mainLayout = findViewById(R.id.mainlayout);
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


                if (d != null) {
                    //Bitmap tempBitmap = Bitmap.createBitmap(720, 1280,
                    //        Bitmap.Config.ARGB_8888);
                    //NetworkDisplay.this.c = new Canvas(tempBitmap);
                    //d.draw();
                    //bitmap720p = tempBitmap;
//
                    //c.drawBitmap(bitmap720p, rect720p, thisRes, defaultPaint);

                    NetworkDisplay.this.c = c;
                    d.draw();

                    d.updateNetwork();
                }
            }
        };//30 fps
        mainLayout.addView(v);

        d = new Drawer(n, this);
        d.windowWidth = screenSize.x;//720
        d.windowHeight = screenSize.y;//1280

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(fontSize);

        halfred = Color.argb(122, 255, 0, 0);
        halfblue = Color.argb(122, 0, 0, 255);
        halfwhite = Color.argb(122, 255, 255, 255);
        fullwhite = Color.argb(255, 255, 255, 255);

        painter = new Paint();

        String in = Data.getData("prevnumbatches", "0");
        what = in;
    }

    String what;

    Paint painter;
    private Paint textPaint;

    int halfred;
    int halfblue;
    int halfwhite;
    int fullwhite;

    //double thicknessMultiplier = 50;

    void setStroke(double value) {
        painter.setStrokeWidth((float) value * 7f);
    }

    void setColor(int c) {
        painter.setColor(c);
    }

    void setColor(int a, int r, int g, int b) {
        painter.setColor(Color.argb(a, r, g, b));
    }

    void drawRect(int x, int y, int width, int height) {
        c.drawRect(x, y, x + width, y + height, painter);
    }

    void drawOval(int x, int y, int diameter1, int diameter2) {
        c.drawOval(x, y, x + diameter1, y + diameter2, painter);
    }

    void drawLine(int x1, int y1, int x2, int y2) {
        c.drawLine(x1, y1, x2, y2, painter);
    }

    void drawString(String text, double x, double y, String position, int bg) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        double width = bounds.right - bounds.left;
        double height = bounds.bottom - bounds.top;//well, it works
        switch (position) {
            case "topleft":
                y += height;
                break;
            case "topright":
                y += height;
                x -= width;
                break;
            case "topmiddle":
                y += height;
                x -= width / 2.0;
                break;
            case "bottomleft":
                break;
            case "middleleft":
                y += height / 2.0;
                break;
            case "bottomright":
                x -= width;
                break;
            case "middleright":
                y += height / 2.0;
                x -= width;
                break;
            case "bottommiddle":
                x -= width / 2.0;
                break;
            case "middle":
                y += height / 2.0;
                x -= width / 2.0;
                break;
        }
        int currentPaint = painter.getColor();
        painter.setColor(bg);
        c.drawRect((float) x, (float) (y - height), (float) (x + width), (float) y, painter);
        c.drawText(text, (float) x, (float) y, textPaint);
        //c.drawText (bounds.left + ", " + bounds.top + ", " + bounds.right + ", " + bounds.bottom, (float)x, (float)y, textPaint);
        //c.drawText (width + ", " + height, (float)x, (float)y + fontSize, textPaint);
        painter.setColor(currentPaint);
    }

    void drawDouble(double text, int x, int y, String pos, int paint) {
        drawString((Math.round(text * 1000.0) / 1000.0) + "", x, y, pos, paint);
    }


    //should work for even and odd numbers of buttons
    public void setButtonPositions() {//needs to prioritize putting buttons near beginning for non-whole number fitting into rows
        //int number = buttonslayout.getChildCount();

        //int buttonWidth = windowWidth / (number + (number%2 == 1?1:0)) * buttonrows;
        //for (int y = 0; y < buttonrows; y ++)
        //    for (int x = 0; x < number / buttonrows + (number%buttonrows != 0?1:0); x ++) {
        //        buttons.get(x + y * (number / buttonrows)).setBounds(buttonWidth * x, windowHeight - buttonHeight * (y + 1),
        //                buttonWidth - 1, buttonHeight);
        //    }
    }


    private boolean updateNetwork = true;

    private boolean stop = true;

    Drawer d;

    Canvas c;

    @Override
    public void onPause() {
        super.onPause();
        //stopUpdater = true;
    }
    @Override
    public void onResume() {
        super.onResume();
        //stopUpdater = false;
        //updater.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Data.putData("prevnumbatches", n.batchesRun + "");
    }

    private LinearLayout buttonslayout;

    public void addButton(String text, final Action action) {
        final Button b = new Button(getBaseContext());
        b.setText(text);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button pressed: " + b.getText());
                vibrate(50);
                action.thing();
            }
        });
        b.setTextSize(8);
        buttonslayout.addView(b);
    }

    private Vibrator vibrate;

    public void vibrate(final int ms) {
        (new Thread() {
            public void run() {
                vibrate.vibrate(ms);
            }
        }).start();
    }

    private View v;
    private Runnable redrawer = new Runnable() {
        @Override
        public void run() {
            v.invalidate();
        }
    };
    //private Bitmap bitmap720p = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        double x = e.getX();///screenSize.x * 720.0;
        double y = e.getY() - notifbar;///screenSize.y*1280.0;
        //System.out.println (e.toString());
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                d.userClicked((int) x, (int) y);
                return true;
        }
        return false;
    }

    /*public void process() {
        Bitmap tempBitmap = Bitmap.createBitmap(720, 1280,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(tempBitmap);

        //ArrayList<String> displayText = new ArrayList<>();

        {
            //should use draw methods instead to make switching between desktop and mobile easier
            //eg. implements NetworkDrawer or some shit.
            //also for buttons with methods.


            if (updateNetwork)
                updateNetwork();

            try {
                int errorGraphWidth = n.maxErrorRecording;

                double max = 0;
                double min = Double.MAX_VALUE / 2.0;
                int left = windowWidth - errorGraphWidth;//n.errors.size()/2;
                try {
                    for (int x = 1; x < n.errors.size(); x++) {//ignore first one lol
                        if (n.errors.get(x) > max)
                            max = n.errors.get(x);
                        if (n.errors.get(x) < min)
                            min = n.errors.get(x);
                    }
                    for (int x = 1; x < n.errors.size(); x++) {//ignore first one lol
                        int xPos = left + (int) (1.0 * x * errorGraphWidth / n.errors.size());
                        try {
                            int yPos = (int) (((max - min) - (n.errors.get(x) - min)) * 100 / (max - min));
                            c.drawRect(xPos-1, yPos-1, xPos + 1, yPos + 1, black);
                        } catch (Exception f) {
                            f.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                c.drawLine(left - 1, 0, left - 1, 101, black);
                c.drawLine(left - 1, 101, left + errorGraphWidth, 101, black);

                drawString(max + "", left - 1, -1, c, Pos.TOP_RIGHT);
                drawString("error", left - 10, 50, c, Pos.CENTER_RIGHT);
                drawString(min + "", left - 1, 101, c, Pos.BOTTOM_RIGHT);
                drawString((n.batchesRun > n.maxErrorRecording ? n.batchesRun - n.maxErrorRecording : 0) + "", left, 101, c, Pos.TOP_LEFT);
                drawString("batch number", left + errorGraphWidth / 2, 111, c, Pos.TOP_CENTER);
                drawString(n.batchesRun + "", left + errorGraphWidth, 101, c, Pos.TOP_RIGHT);

                //if (n.errors.size() > 0)
                //    displayText.add ("current errors: " + n.errors.get(Math.min (n.errors.size(), Network.maxErrorSize)-1)+"");

                Paint tempPaint = new Paint();

                for (int x = 0; x < layers.size(); x++) {
                    int nneurons = layers.get(x).size();
                    for (int y = 0; y < nneurons; y++) {
                        XY thisNeuron = positions.get(x).get(y);

                        for (int z = 0; z < layers.get(x).get(y).inputs.size(); z++) {
                            XY neuronPos = getPosition(layers.get(x).get(y).inputs.get(z).n1);
                            XY inputNeuron = positions.get(neuronPos.x).get(neuronPos.y);

                            double val = layers.get(x).get(y).inputs.get(z).w;


                            if (val > 0) {
                                tempPaint.setStrokeWidth((float) Math.sqrt(val * thicknessMultiplier));
                                tempPaint.setColor(halfblue.getColor());
                            } else {
                                tempPaint.setStrokeWidth((float) Math.sqrt(val * -thicknessMultiplier));
                                tempPaint.setColor(halfred.getColor());
                            }
                            c.drawLine(inputNeuron.x + neuronRadius, inputNeuron.y, thisNeuron.x - neuronRadius, thisNeuron.y, tempPaint);

                            drawDouble(val, (thisNeuron.x + inputNeuron.x) / 2, (thisNeuron.y + inputNeuron.y) / 2, c, Pos.CENTER_CENTER);
                        }
                    }
                }

                for (int x = 0; x < layers.size(); x++) {
                    int nneurons = layers.get(x).size();
                    for (int y = 0; y < nneurons; y++) {
                        XY thisNeuron = positions.get(x).get(y);
                        Neuron neuron = layers.get(x).get(y);

                        float extra;
                        double bias = neuron.bias;
                        if (bias > 0) {
                            //tempPaint.setStrokeWidth((float) Math.sqrt(bias * thicknessMultiplier));
                            extra = (float) Math.sqrt(bias * thicknessMultiplier);
                            tempPaint.setColor(halfblue.getColor());
                        } else {
                            //tempPaint.setStrokeWidth((float) Math.sqrt(bias * -thicknessMultiplier));
                            extra = (float) Math.sqrt(bias * -thicknessMultiplier);
                            tempPaint.setColor(halfred.getColor());
                        }
                        c.drawOval(thisNeuron.x - neuronRadius - extra,
                                thisNeuron.y - neuronRadius - extra,
                                thisNeuron.x + neuronRadius + extra,
                                thisNeuron.y + neuronRadius + extra, tempPaint);
                        drawDouble(neuron.value, thisNeuron.x, thisNeuron.y, c, Pos.CENTER_CENTER);
                        drawDouble(bias, thisNeuron.x, thisNeuron.y + neuronRadius + 10, c, Pos.CENTER_CENTER);
                    }
                }

                for (int x = 0; x < n.inputs.size(); x++) {
                    XY pos0 = getPosition(n.inputs.get(x));
                    XY pos = positions.get(pos0.x).get(pos0.y);
                    drawString("in " + x, pos.x - neuronRadius * 2, pos.y, c, Pos.CENTER_CENTER);
                }
                for (int x = 0; x < n.outputs.size(); x++) {
                    XY pos0 = getPosition(n.outputs.get(x));
                    XY pos = positions.get(pos0.x).get(pos0.y);
                    drawString("out " + x, pos.x + neuronRadius * 2, pos.y, c, Pos.CENTER_CENTER);
                }

                //plot the data?

                int dataSquareSide = 100;

                left = windowWidth - dataSquareSide;
                int top = windowHeight / 2 - dataSquareSide / 2;

                double[] maxes = n.inMaxes;
                double[] mins = n.inMins;

                //displayText.add (maxes[0]+" " + mins[0]);
                //displayText.add (maxes[1]+" " + mins[1]);
                //displayText.add (maxes[2]+" " + mins[2]);

                if (n.thisBatchPoints.get(0).length == 3 && n.inputs.size() == 2) {

                    drawDouble(maxes[0], windowWidth, top + dataSquareSide, c, Pos.TOP_RIGHT);
                    drawDouble(mins[0], windowWidth - dataSquareSide, top + dataSquareSide, c, Pos.TOP_LEFT);

                    drawDouble(maxes[1], windowWidth - dataSquareSide, top, c, Pos.TOP_RIGHT);
                    drawDouble(mins[1], windowWidth - dataSquareSide, top + dataSquareSide, c, Pos.BOTTOM_RIGHT);

                    for (int x = 0; x < n.thisBatchPoints.size(); x++) {
                        double[] thisCase = n.thisBatchPoints.get(x);

                        int xPosition = (int) (left + dataSquareSide * ((thisCase[0] - mins[0]) / (maxes[0] - mins[0])));
                        int yPosition = (int) (top + dataSquareSide - dataSquareSide * ((thisCase[1] - mins[1]) / (maxes[1] - mins[1])));
                        int color = (int) (((thisCase[2] - mins[2]) / (maxes[2] - mins[2])) * 255.0 * 2.0 - 255.0);

                        //if (x == 0){
                        //    displayText.add (xPosition + ", " + yPosition + " - " + color);
                        //}

                        if (color > 255)
                            color = 255;
                        if (color < -255)
                            color = -255;

                        if (color < 0) {
                            color = -color;
                            tempPaint.setARGB(255, color, 0, 0);
                        } else
                            tempPaint.setARGB(255, 0, 0, color);
                        tempPaint.setStrokeWidth(1);

                        c.drawRect(xPosition-1, yPosition-1, xPosition + 1, yPosition + 1, tempPaint);
                    }
                }

                displayText.add ("test case: " + n.testCase);

                displayText.add("lr: " + n.lr);

                for (int x = 0; x < displayText.size(); x++)
                    c.drawText(displayText.get(x), 0, fontSize * (x + 1), textPaint);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            

        }
        bitmap720p = tempBitmap;
    }*/

    /*ArrayList<ArrayList<Neuron>> layers = new ArrayList<>();
    ArrayList<ArrayList<XY>> positions = new ArrayList<>();

    private XY getPosition(Neuron n) {
        for (int x = 0; x < layers.size(); x++) {
            for (int y = 0; y < layers.get(x).size(); y++) {
                if (layers.get(x).get(y) == n)
                    return new XY(x, y);
            }
        }
        System.out.println("neuron not found");
        return new XY (-1, -1);
    }*/

    //private boolean working = false;

    /*public void updateNetwork() {
        if (updateNetwork && !working) {
            updateNetwork = false;
            working = true;
            ArrayList<Neuron> neurons = n.neurons;
            layers = new ArrayList<>();
            layers.add(new ArrayList<Neuron>());
            for (int x = 0; x < neurons.size(); x++) {
                //boolean incrementLayer = false; //idk
                for (int y = 0; y < neurons.get(x).inputs.size(); y++) {
                    for (int z = 0; z < layers.get(layers.size() - 1).size(); z++) {
                        if (neurons.get(x).inputs.get(y).n1 == layers.get(layers.size() - 1).get(z))
                            layers.add(new ArrayList<Neuron>());
                    }
                }
                layers.get(layers.size() - 1).add(neurons.get(x));
            }

            int width = layers.size();

            positions = new ArrayList<>();
            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                positions.add(new ArrayList<XY>());
                for (int y = 0; y < nneurons; y++) {
                    positions.get(x).add(new XY((int) (windowWidth * (x + 0.5) / (width + 1)), windowHeight * (y + 2) / (nneurons + 3)));

                }

            }

            setButtonPositions();


            working = false;
        }
    }*/



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
