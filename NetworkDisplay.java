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

import java.util.ArrayList;
import java.util.Arrays;

public class NetworkDisplay extends AppCompatActivity {
    //changeables
    private int neuronRadius = 20;
    private double thicknessMultiplier = 50;
    private int fontSize = 12;

    //useables
    public static final Point screenSize = new Point();

    private Network n = new Network();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_display);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(fontSize);

        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);
        int resId = getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resId > 0) {
            screenSize.y -= getResources().getDimensionPixelSize(resId);
        }
        //windowWidth = screenSize.x;
        //windowHeight = screenSize.y;


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
                c.drawBitmap(bitmap720p, rect720p, thisRes, defaultPaint);
            }
        };//30 fps
        mainLayout.addView(v);

        addButton("new training batch", new Action() {
            public void thing() {
                n.setQAArray();
            }
        });
        addButton("train one batch", new Action() {
            public void thing() {
                n.runBatch();
            }
        });
        addButton(">/II slow training", new Action() {
            public void thing() {
                if (stop) {
                    stop = false;
                    Thread tempThread = new Thread() {
                        public void run() {
                            long time = System.currentTimeMillis();
                            while (!stop) {
                                //n.runBatch();
                                //try {
                                //    Thread.sleep(30);
                                //}
                                //catch (Exception f){
                                //    System.out.println ("sleeping failed");
                                //}

                                try {
                                    Thread.sleep((int) (Math.max(-(System.currentTimeMillis() - time - 30), 0)));
                                } catch (Exception g) {
                                    System.out.println("um");
                                }
                                time = System.currentTimeMillis();
                                n.runBatch();
                            }
                        }
                    };
                    tempThread.start();

                } else
                    stop = true;
            }
        });

        addButton(">/II fast training", new Action() {
            public void thing() {
                if (stop) {
                    stop = false;
                    Thread tempThread = new Thread() {
                        public void run() {
                            while (!stop) {
                                n.runBatch();
                            }
                        }
                    };
                    tempThread.start();
                } else
                    stop = true;
            }
        });
        addButton("lr * 2", new Action() {
            public void thing() {
                n.lr *= 2.0;
            }
        });
        addButton("lr / 2", new Action() {
            public void thing() {
                n.lr /= 2.0;
            }
        });
        addButton("update network", new Action() {
            public void thing() {
                updateNetwork = true;
            }
        });

        halfred.setARGB(122, 255, 0, 0);
        halfblue.setARGB(122, 0, 0, 255);
        halfwhite.setARGB(122, 255, 255, 255);
        black.setARGB(255, 0, 0, 0);
        black.setStrokeWidth(1);
    }

    //should work for even and odd numbers of buttons
    private void setButtonPositions() {//needs to prioritize putting buttons near beginning for non-whole number fitting into rows
        //int number = buttonslayout.getChildCount();

        //int buttonWidth = windowWidth / (number + (number%2 == 1?1:0)) * buttonrows;
        //for (int y = 0; y < buttonrows; y ++)
        //    for (int x = 0; x < number / buttonrows + (number%buttonrows != 0?1:0); x ++) {
        //        buttons.get(x + y * (number / buttonrows)).setBounds(buttonWidth * x, windowHeight - buttonHeight * (y + 1),
        //                buttonWidth - 1, buttonHeight);
        //    }
    }

    int windowWidth = 720;
    int windowHeight = 1280;

    Paint halfred = new Paint();
    Paint halfblue = new Paint();
    Paint halfwhite = new Paint();
    Paint black = new Paint();

    private boolean updateNetwork = true;

    private boolean stop = true;

    private boolean stopUpdater = false;
    Thread updater = (new Thread() {
        long time = System.currentTimeMillis();

        @Override
        public void run() {
            while (!stopUpdater) {
                process();
                try {
                    Thread.sleep((int) (Math.max(-(System.currentTimeMillis() - time - 16), 0)));
                } catch (Exception e) {
                    System.out.println("um");
                }
                time = System.currentTimeMillis();
            }
        }
    });

    @Override
    public void onPause() {
        super.onPause();
        stopUpdater = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        stopUpdater = false;
        updater.start();
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
        b.setTextSize(5);
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
    private Bitmap bitmap720p = Bitmap.createBitmap(720, 1280,
            Bitmap.Config.ARGB_8888);
    public void process() {
        Bitmap tempBitmap = Bitmap.createBitmap(720, 1280,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(tempBitmap);

        {
            if (updateNetwork) {
                updateNetwork();
            }

            //drawString("topleft", 0, 0, c, Pos.TOP_LEFT);
            //drawString("topcenter", 360, 0, c, Pos.TOP_CENTER);
            //drawString("topright", 720, 0, c, Pos.TOP_RIGHT);
//
            //drawString("middleleft", 0, 640, c, Pos.CENTER_LEFT);
            //drawString("poop", 360, 640, c, Pos.CENTER_CENTER);
            //drawString("middleright", 720, 640, c, Pos.CENTER_RIGHT);
//
            //drawString("bottomleft", 0, 1280, c, Pos.BOTTOM_LEFT);
            //drawString ("bottommiddle", 360, 1280, c, Pos.BOTTOM_CENTER);
            //drawString("bottomright", 720, 1280, c, Pos.BOTTOM_RIGHT);

            try {
                displayText = new ArrayList();

                int errorGraphWidth = windowWidth * 7 / 10;

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
                            c.drawRect(xPos, yPos, xPos + 1, yPos + 1, black);
                        } catch (Exception f) {
                        }
                    }
                } catch (Exception e) {
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

                        double bias = neuron.bias;
                        if (bias > 0) {
                            tempPaint.setStrokeWidth((float) Math.sqrt(bias * thicknessMultiplier));
                            tempPaint.setColor(halfblue.getColor());
                        } else {
                            tempPaint.setStrokeWidth((float) Math.sqrt(bias * -thicknessMultiplier));
                            tempPaint.setColor(halfred.getColor());
                        }
                        c.drawOval(thisNeuron.x - neuronRadius,
                                thisNeuron.y - neuronRadius,
                                thisNeuron.x - neuronRadius + neuronRadius * 2,
                                thisNeuron.y - neuronRadius + neuronRadius * 2, tempPaint);
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

                double[] maxes = new double[n.thisBatchPoints.get(0).length];
                double[] mins = new double[n.thisBatchPoints.get(0).length];

                Arrays.fill(maxes, Double.MIN_VALUE / 2.0);
                Arrays.fill(mins, Double.MAX_VALUE / 2.0);

                for (int x = 0; x < n.thisBatchPoints.size(); x++) {
                    for (int y = 0; y < n.thisBatchPoints.get(0).length; y++) {
                        if (n.thisBatchPoints.get(x)[y] > maxes[y])
                            maxes[y] = n.thisBatchPoints.get(x)[y];
                        if (n.thisBatchPoints.get(x)[y] < mins[y])
                            mins[y] = n.thisBatchPoints.get(x)[y];
                    }
                }
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

                        c.drawRect(xPosition, yPosition, xPosition + 1, yPosition + 1, tempPaint);
                    }
                }

                displayText.add("lr: " + n.lr);

                for (int x = 0; x < displayText.size(); x++)
                    c.drawText(displayText.get(x), 0, fontSize * (x + 1), textPaint);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            
            
        }
        bitmap720p = tempBitmap;
    }

    ArrayList<ArrayList<Neuron>> layers = new ArrayList();
    ArrayList<ArrayList<XY>> positions = new ArrayList();

    private XY getPosition(Neuron n) {
        for (int x = 0; x < layers.size(); x++) {
            for (int y = 0; y < layers.get(x).size(); y++) {
                if (layers.get(x).get(y) == n)
                    return new XY(x, y);
            }
        }
        System.out.println("neuron not found");
        return null;
    }

    private boolean working = false;

    public void updateNetwork() {
        if (updateNetwork && !working) {
            updateNetwork = false;
            working = true;
            ArrayList<Neuron> neurons = n.neurons;
            layers = new ArrayList<ArrayList<Neuron>>();
            layers.add(new ArrayList<Neuron>());
            for (int x = 0; x < neurons.size(); x++) {
                //boolean incrementLayer = false;
                for (int y = 0; y < neurons.get(x).inputs.size(); y++) {
                    for (int z = 0; z < layers.get(layers.size() - 1).size(); z++) {
                        if (neurons.get(x).inputs.get(y).n1 == layers.get(layers.size() - 1).get(z))
                            layers.add(new ArrayList<Neuron>());
                    }
                }
                layers.get(layers.size() - 1).add(neurons.get(x));
            }

            int width = layers.size();

            positions = new ArrayList();
            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                positions.add(new ArrayList());
                for (int y = 0; y < nneurons; y++) {
                    positions.get(x).add(new XY((int) (windowWidth * (x + 0.5) / (width + 1)), windowHeight * (y + 2) / (nneurons + 3)));

                }

            }

            setButtonPositions();


            working = false;
        }
    }

    private ArrayList<String> displayText = new ArrayList();

    private Paint textPaint;

    private enum Pos {
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

    private void drawDouble(double text, int x, int y, Canvas c, Pos pos) {
        drawString((Math.round(text * 1000.0) / 1000.0) + "", x, y, c, pos);
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
