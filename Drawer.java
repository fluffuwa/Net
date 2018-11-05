package com.example.joeym.playground;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;

class Drawer {
    private final Network n;
    private final NetworkDisplay nd;

    Drawer(final Network n, NetworkDisplay nd) {
        this.n = n;
        this.nd = nd;

        nd.addButton("new case", new Action() {
            void thing() {
                n.testCase++;
                if (n.testCase > QA.maxQA)
                    n.testCase = 1;
                n.setQAArray();
                n.setNetworkForQA();
                updateNetwork = true;
            }
        });
        nd.addButton("train one batch", new Action() {
            void thing() {
                n.runBatch();
            }
        });
        nd.addButton(">/II slow training", new Action() {
            void thing() {
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
                                    time = System.currentTimeMillis();
                                    n.runBatch();
                                } catch (Exception g) {
                                    System.out.println("um");
                                }
                            }
                        }
                    };
                    tempThread.start();

                } else
                    stop = true;
            }
        });
        nd.addButton(">/II fast training", new Action() {
            void thing() {
                if (stop) {
                    stop = false;
                    Thread tempThread = new Thread() {
                        public void run() {
                            while (!stop) {
                                try {
                                    n.runBatch();
                                } catch (Exception e) {
                                }
                            }
                        }
                    };
                    tempThread.start();
                } else
                    stop = true;
            }
        });
        nd.addButton("lr * 2", new Action() {
            void thing() {
                n.lr *= 2.0;
            }
        });
        nd.addButton("lr / 2", new Action() {
            void thing() {
                n.lr /= 2.0;
            }
        });
        //nd.addButton ("update network", new Action () {
        //void thing() {updateNetwork = true;}});
    }

    boolean stop = true;

    int windowWidth;
    int windowHeight;


    private XY selectedNeuron;

    void userClicked(int xPos, int yPos) {
        //select the neuron
        for (int x = 0; x < positions.size(); x++) {
            for (int y = 0; y < positions.get(x).size(); y++) {
                XY neuronPosition = positions.get(x).get(y);
                if (xPos > neuronPosition.x - nd.neuronRadius &&
                        xPos < neuronPosition.x + nd.neuronRadius &&
                        yPos > neuronPosition.y - nd.neuronRadius &&
                        yPos < neuronPosition.y + nd.neuronRadius) {
                    //System.out.println (x + ", " + y);
                    selectedNeuron = new XY(x, y);
                    n.selectedNeuron = layers.get(x).get(y);
                    return; //if a neuron was pressed, exit this method so the click isn't "reused".
                }
            }
        }
    }

    private ArrayList<ArrayList<Neuron>> layers = new ArrayList<>();
    private ArrayList<ArrayList<XY>> positions = new ArrayList<>();

    void draw() {
        try {
            ArrayList<String> displayText = new ArrayList<>();
            int neuronRadius = nd.neuronRadius;
            int fontSize = nd.fontSize;

            int errorGraphWidth = windowWidth * 7 / 10;

            double max = 0;
            double min = Double.MAX_VALUE / 2.0;
            int left = windowWidth - errorGraphWidth;
            try {
                for (int x = 0; x < n.errors.size(); x++) {
                    if (n.errors.get(x) > max)
                        max = n.errors.get(x);
                    if (n.errors.get(x) < min)
                        min = n.errors.get(x);//if you just set the min as the last number, the graph will jump around a lot due to random error.
                }

                nd.setColor(Color.BLACK);

                //double count = 0;
                //delete//for (int x = 0; x < n.errors.size(); x++) {
                //delete//    if (count/errorGraphWidth < 1.0*x/n.errors.size()) {
                //delete//        count++;
                //delete//    }
                //delete//    else{//could run this part many many times. would have to do the above calculation that many times which is also expensive.
                //delete//        continue;
                //delete//    }
                //delete//    coun2 ++;
                for (double x = 0; x < n.errors.size(); x += Math.max(1.0 * n.errors.size() / errorGraphWidth, 1)) {//max is errorGraphWidth number of calculations
                    int xPos = left + (int) (1.0 * x * errorGraphWidth / n.errors.size());
                    //count ++;
                    try {
                        int yPos = (int) (((max - min) - (n.errors.get((int) x) - min)) * 100 / (max - min));
                        nd.drawRect(xPos, yPos, 1, 1);
                    } catch (Exception f) {
                    }
                }
                //displayText.add (count+"");
            } catch (Exception e) {
            }
            nd.setStroke(1.0 / 7.0);
            nd.setColor(Color.BLACK);
            nd.drawLine(left - 1, 0, left - 1, 101);
            nd.drawLine(left - 1, 101, left + errorGraphWidth, 101);

            nd.drawString(max + "", left - 1, -1, "topright", nd.halfwhite);
            nd.drawString("error", left - 10, 50, "middleright", nd.halfwhite);
            nd.drawString(min + "", left - 1, 101, "bottomright", nd.halfwhite);
            nd.drawString((n.batchesRun > n.maxErrorRecording ? n.batchesRun - n.maxErrorRecording : 0) + "", left, 101, "topleft", nd.halfwhite);
            nd.drawString("batch number", left + errorGraphWidth / 2, 111, "topmiddle", nd.halfwhite);
            nd.drawString(n.batchesRun + "", left + errorGraphWidth, 101, "topright", nd.halfwhite);

            //if (n.errors.size() > 0)
            //    displayText.add ("current errors: " + n.errors.get(Math.min (n.errors.size(), Network.maxErrorSize)-1)+"");

            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                for (int y = 0; y < nneurons; y++) {
                    XY thisNeuron = positions.get(x).get(y);

                    for (int z = 0; z < layers.get(x).get(y).inputs.size(); z++) {
                        XY neuronPos = getNeuronLocationInArrayArray(layers.get(x).get(y).inputs.get(z).n1);
                        XY inputNeuron = positions.get(neuronPos.x).get(neuronPos.y);


                        double val = layers.get(x).get(y).inputs.get(z).w;
                        if (val > 0) {
                            nd.setStroke(Math.sqrt(val));
                            nd.setColor(nd.halfblue);
                        } else {
                            nd.setStroke(Math.sqrt(-val));
                            nd.setColor(nd.halfred);
                        }
                        nd.drawLine(inputNeuron.x + neuronRadius, inputNeuron.y, thisNeuron.x - neuronRadius, thisNeuron.y);

                        if ((selectedNeuron.x == x && selectedNeuron.y == y) ||
                                (neuronPos.x == selectedNeuron.x && neuronPos.y == selectedNeuron.y))
                            ;
                        else
                            nd.drawDouble(val, (thisNeuron.x + inputNeuron.x) / 2, (thisNeuron.y + inputNeuron.y) / 2, "middle", nd.halfwhite);
                    }
                }
            }
            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                for (int y = 0; y < nneurons; y++) {
                    XY thisNeuron = positions.get(x).get(y);

                    for (int z = 0; z < layers.get(x).get(y).inputs.size(); z++) {
                        XY neuronPos = getNeuronLocationInArrayArray(layers.get(x).get(y).inputs.get(z).n1);
                        XY inputNeuron = positions.get(neuronPos.x).get(neuronPos.y);


                        if ((selectedNeuron.x == x && selectedNeuron.y == y) ||
                                (neuronPos.x == selectedNeuron.x && neuronPos.y == selectedNeuron.y)) {
                            double val = layers.get(x).get(y).inputs.get(z).w;
                            nd.drawDouble(val, (thisNeuron.x + inputNeuron.x) / 2, (thisNeuron.y + inputNeuron.y) / 2, "middle", nd.fullwhite);
                        }
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
                        nd.setStroke(bias);
                        nd.setColor(Color.BLUE);
                    } else {
                        nd.setStroke(-bias);
                        nd.setColor(Color.RED);
                    }
                    nd.drawOval(thisNeuron.x - neuronRadius,
                            thisNeuron.y - neuronRadius,
                            neuronRadius * 2, neuronRadius * 2);
                    if (neuron == n.selectedNeuron) {
                        nd.drawDouble(neuron.value, thisNeuron.x, thisNeuron.y, "middle", nd.fullwhite);
                        nd.drawDouble(bias, thisNeuron.x, thisNeuron.y + neuronRadius + 10, "middle", nd.fullwhite);
                    } else {
                        nd.drawDouble(neuron.value, thisNeuron.x, thisNeuron.y, "middle", nd.halfwhite);
                        nd.drawDouble(bias, thisNeuron.x, thisNeuron.y + neuronRadius + 10, "middle", nd.halfwhite);
                    }
                }

            }

            for (int x = 0; x < n.inputs.size(); x++) {
                XY pos0 = getNeuronLocationInArrayArray(n.inputs.get(x));
                XY pos = positions.get(pos0.x).get(pos0.y);
                nd.drawString("in " + x, pos.x - neuronRadius * 2, pos.y, "middle", nd.halfwhite);
            }
            for (int x = 0; x < n.outputs.size(); x++) {
                XY pos0 = getNeuronLocationInArrayArray(n.outputs.get(x));
                XY pos = positions.get(pos0.x).get(pos0.y);
                nd.drawString("out " + x, pos.x + neuronRadius * 2, pos.y, "middle", nd.halfwhite);
            }



            //plot the data?

            int dataSquareSide = 100;

            left = windowWidth - dataSquareSide;
            int top = windowHeight / 2 - dataSquareSide / 2;

            ArrayList<double[]> lastBatch = n.thisBatchPoints;

            double[] maxes = new double[lastBatch.get(0).length];
            double[] mins = new double[lastBatch.get(0).length];

            Arrays.fill(maxes, Double.MIN_VALUE / 2.0);
            Arrays.fill(mins, Double.MAX_VALUE / 2.0);
//
            //for (int x = 0; x < lastBatch.size(); x ++){
            //    for (int y = 0; y < lastBatch.get(0).length; y ++){
            //        if (lastBatch.get (x) [y] > maxes [y])
            //            maxes [y] = lastBatch.get(x)[y];
            //        if (lastBatch.get(x)[y] < mins [y])
            //            mins [y] = lastBatch.get(x)[y];
            //    }
            //}
            for (double[] x : lastBatch) {
                for (int y = 0; y < lastBatch.get(0).length; y++) {
                    if (x[y] > maxes[y])
                        maxes[y] = x[y];
                    if (x[y] < mins[y])
                        mins[y] = x[y];
                }
            }

            if (n.inputs.size() == 1) {
                nd.drawString(selectedNeuron.x + ", " + selectedNeuron.y, windowWidth - dataSquareSide / 2, top - 1 - fontSize, "bottommiddle", nd.halfwhite);
                nd.drawString(((int) (mins[1] * 1000)) / 1000.0 + " to " + ((int) (maxes[1] * 1000)) / 1000.0, windowWidth, top - 1, "bottomright", nd.halfwhite);

                nd.drawDouble(maxes[0], windowWidth, top + dataSquareSide + 1, "topright", nd.halfwhite);
                nd.drawDouble(mins[0], windowWidth - dataSquareSide - 1, top + dataSquareSide + 1, "topleft", nd.halfwhite);

                nd.drawDouble(maxes[1], windowWidth - dataSquareSide - 1, top, "topright", nd.halfwhite);
                nd.drawDouble(mins[1], windowWidth - dataSquareSide - 1, top + dataSquareSide, "bottomright", nd.halfwhite);

                for (int x = 0; x < lastBatch.size(); x++) {

                    double[] thisCase = lastBatch.get(x);

                    int xPosition = (int) (left + dataSquareSide * ((thisCase[0] - mins[0]) / (maxes[0] - mins[0])));
                    int yPosition = (int) (top + dataSquareSide - dataSquareSide * ((thisCase[1] - mins[1]) / (maxes[1] - mins[1])));
                    nd.setColor(255, 0, 0, 0);

                    nd.drawRect(xPosition, yPosition, 1, 1);
                }


            }
            if (n.inputs.size() == 2 /*&& maxes [2] <= 1 && mins [2] >= -1*//*n.selectedNeuron.functionType == Neuron.FunctionType.SIGMOID*/) {

                int rectRadiusish = 12;

                nd.drawString(selectedNeuron.x + ", " + selectedNeuron.y,
                        windowWidth - dataSquareSide / 2 - rectRadiusish,
                        top - rectRadiusish - 1 - fontSize, "bottommiddle", nd.halfwhite);
                nd.drawString(((int) (mins[2] * 1000)) / 1000.0 + "(red) to " + ((int) (maxes[2] * 1000)) / 1000.0 + "(blue)", windowWidth, top - 1 - rectRadiusish, "bottomright", nd.halfwhite);

                nd.drawDouble(maxes[0], windowWidth, top + dataSquareSide + rectRadiusish + 1, "topright", nd.halfwhite);
                nd.drawDouble(mins[0], windowWidth - dataSquareSide - rectRadiusish * 2, top + dataSquareSide + rectRadiusish + 1, "topleft", nd.halfwhite);

                nd.drawDouble(maxes[1], windowWidth - dataSquareSide - rectRadiusish * 2 - 1, top - rectRadiusish, "topright", nd.halfwhite);
                nd.drawDouble(mins[1], windowWidth - dataSquareSide - rectRadiusish * 2 - 1, top + dataSquareSide + rectRadiusish, "bottomright", nd.halfwhite);

                for (int x = 0; x < lastBatch.size(); x++) {

                    double[] thisCase = lastBatch.get(x);

                    int xPosition = (int) (left + dataSquareSide * ((thisCase[0] - mins[0]) / (maxes[0] - mins[0])) - rectRadiusish);
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
                        nd.setColor(30, color, 0, 0);
                    } else
                        nd.setColor(30, 0, 0, color);

                    //nd.drawRect (xPosition, yPosition, 1, 1);
                    //nd.drawRect (xPosition-1, yPosition-1, 3, 3);
                    //nd.drawSplotch (xPosition - 3, yPosition - 3, 6);
                    nd.drawRect(xPosition - rectRadiusish, yPosition - rectRadiusish,
                            rectRadiusish * 2 + 1, rectRadiusish * 2 + 1);
                    //nd.drawOval (xPosition - 6, yPosition - 6, 12, 12);
                    //nd.drawOval (xPosition - 3, yPosition - 3, 6, 6);
                    //nd.setAlpha (122);

                    //a lot: 13 seconds
                    //just one: 12.66 seconds
                    //smol rects: 12 seconds
                    //many ovals: 12 seconds
                }
            }

            displayText.add("test case: " + n.testCase);

            displayText.add("lr: " + n.lr);

            nd.setColor(Color.BLACK);
            for (int x = 0; x < displayText.size(); x++)
                nd.drawString(displayText.get(x), 0, fontSize * x, "topleft", nd.halfwhite);
        } catch (Exception e) {
            //System.out.println (e.toString().substring (0, 20));
            e.printStackTrace();
            n.setQAArray();
            n.setNetworkForQA();
            updateNetwork = true;
        }
    }



    boolean updateNetwork = true;
    private boolean working = false;

    public void updateNetwork() {
        if (updateNetwork && !working) {
            updateNetwork = false;
            working = true;
            ArrayList<Neuron> neurons = n.neurons;
            layers = new ArrayList<ArrayList<Neuron>>();
            layers.add(new ArrayList<Neuron>());
            for (int x = 0; x < neurons.size(); x++) {
                boolean incrementLayer = false;
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

            selectedNeuron = getNeuronLocationInArrayArray(n.outputs.get(0));
            n.selectedNeuron = n.outputs.get(0);

            nd.setButtonPositions();

            working = false;
        }
    }

    private XY getNeuronLocationInArrayArray(Neuron n) {
        for (int x = 0; x < layers.size(); x++) {
            for (int y = 0; y < layers.get(x).size(); y++) {
                if (layers.get(x).get(y) == n)
                    return new XY(x, y);
            }
        }
        System.out.println("neuron not found");
        return new XY(-1, -1);
    }

}
