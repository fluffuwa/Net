package com.example.joeym.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Network {//maybe wants some way to link multiple neurons together? for convolutional layers y'know


    int testCase = QA.maxQA;
    int[] hiddenLayerSizes = {8, 7};

    int batchSize = 1000;

    int maxErrorRecording = NetworkDisplay.maxErrorRecording;

    double lr = 0.1;

    public Network() {

        setQAArray();

        setNetworkForQA();

        //runBatch();
    }

    int batchesRun = 0;

    ArrayList<Neuron> neurons = new ArrayList<Neuron>();

    ArrayList<Neuron> inputs = new ArrayList<Neuron>();
    ArrayList<Neuron> outputs = new ArrayList<Neuron>();

    ArrayList<Double> errors = new ArrayList<>();
    ArrayList<QA> QAArray = new ArrayList<>();

    public void setQAArray() {
        ArrayList<QA> tempQAArray = new ArrayList<>();
        for (int x = 0; x < batchSize; x++)
            tempQAArray.add(new QA(testCase));
        QAArray = tempQAArray;
    }


    public void setNetworkForQA() {
        neurons = new ArrayList<>();
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();

        for (int x = 0; x < QAArray.get(0).realInputs.length; x++)
            addInput();
        boolean[] sigmoidOrLinear = new boolean[QAArray.get(0).realOutputs.length];//true means sigmoid
        Arrays.fill(sigmoidOrLinear, true);
        for (int x = 0; x < QAArray.size(); x++) {
            for (int y = 0; y < QAArray.get(x).realOutputs.length; y++)
                if (QAArray.get(x).realOutputs[y] != 0 && QAArray.get(x).realOutputs[y] != 1)
                    sigmoidOrLinear[y] = false;
        }
        for (int x = 0; x < QAArray.get(0).realOutputs.length; x++)
            addOutput(sigmoidOrLinear[x] ? Neuron.FunctionType.SIGMOID : Neuron.FunctionType.LINEAR);

        ArrayList<Neuron> prevLayer = inputs;
        for (int x = 0; x < hiddenLayerSizes.length; x++) {
            ArrayList<Neuron> thisLayer = new ArrayList<Neuron>();
            for (int y = 0; y < hiddenLayerSizes[x]; y++)
                thisLayer.add(addHidden());
            connect(prevLayer, thisLayer);
            prevLayer = thisLayer;
        }
        connect(prevLayer, outputs);

        selectedNeuron = outputs.get(0);

        thisBatchPoints = new ArrayList<>();

        for (int count2 = 0; count2 < batchSize; count2++) {
            double[] record = new double[inputs.size() + 1];
            for (int x = 0; x < inputs.size(); x++) {
                record[x] = QAArray.get(count2).realInputs[x];
            }
            record[inputs.size()] = selectedNeuron.value;
            thisBatchPoints.add(record);
        }

        sortNeurons();
    }

    Neuron selectedNeuron;

    //maybe also include mins and maxes for that batch of runs?.. in either case it flashes though I think
    public ArrayList<double[]> thisBatchPoints;//for plotting on the display

    public void runBatch() {
        ArrayList<double[]> nextBatchPoints = new ArrayList<>();
        batchesRun++;
        double totalError = 0;
        Collections.shuffle(QAArray);//pretty much always good to shuffle it
        for (int count2 = 0; count2 < batchSize; count2++) {
            QA test = QAArray.get(count2);

            double[] results = feedForward(test.realInputs);

            double[] record = new double[inputs.size() + 1];
            for (int x = 0; x < inputs.size(); x++) {
                record[x] = test.realInputs[x];
            }
            record[inputs.size()] = selectedNeuron.value;
            nextBatchPoints.add(record);

            totalError += (backprop(test.realOutputs));
        }
        if (errors.size() == maxErrorRecording)
            errors.remove(0);
        errors.add(totalError / batchSize);
        thisBatchPoints = nextBatchPoints;
    }


    //public static void main(String[] args) {
    //    Network n = new Network();
    //}

    //returns output neuron values
    public double[] feedForward(double[] inputValues) {
        sortNeurons();
        if (inputValues.length != inputs.size())
            System.out.println("real input size not equal to net input size");
        for (int x = 0; x < inputs.size(); x++)
            inputs.get(x).bias = inputValues[x];
        for (int x = 0; x < neurons.size(); x++)
            neurons.get(x).forward();
        double[] outputValues = new double[outputs.size()];
        for (int x = 0; x < outputValues.length; x++)
            outputValues[x] = outputs.get(x).value;
        return outputValues;
    }

    //returns error value
    public double backprop(double[] realValues) {
        //sortNeurons(); shouldn't need to sort.
        if (realValues.length != outputs.size())
            System.out.println("net output and real output not same size");
        for (int x = 0; x < outputs.size(); x++)
            outputs.get(x).error = outputs.get(x).value - realValues[x];
        for (int x = neurons.size() - 1; x >= 0; x--)
            neurons.get(x).backward(lr);

        double error = 0;
        for (int x = 0; x < outputs.size(); x++)
            error += (Math.pow(outputs.get(x).error, 2) / 2.0);
        //for (int x = 0; x < outputs.size(); x ++)
        //    error -= (realValues [x] * Math.log (outputs.get(x).value));
        error = error / realValues.length;
        return error;
    }


    //remove functions?********************************************************

    //neuron one must be in front (lower position) of neuron two
    //sort the neurons array so it feeds forward
    public void connect(Neuron one, Neuron two) {
        Weight w = new Weight(one, two);
        one.outputs.add(w);
        two.inputs.add(w);
    }

    public Neuron addInput() {
        Neuron two = new Neuron(Neuron.FunctionType.LINEAR);
        neurons.add(0, two);
        inputs.add(two);
        return two;
    }

    public Neuron addHidden() {
        Neuron two = new Neuron(Neuron.FunctionType.SIGMOID);
        neurons.add(two);
        return two;
    }

    public Neuron addOutput(Neuron.FunctionType funct) {
        Neuron two = new Neuron(funct);
        neurons.add(two);
        outputs.add(two);
        return two;
    }

    public void connect(Neuron n, ArrayList<Neuron> neurons2) {
        for (int x = 0; x < neurons2.size(); x++)
            connect(n, neurons2.get(x));
    }

    public void connect(ArrayList<Neuron> neurons1, Neuron n) {
        for (int x = 0; x < neurons1.size(); x++)
            connect(neurons1.get(x), n);
    }

    public void connect(ArrayList<Neuron> neurons1, ArrayList<Neuron> neurons2) {
        for (int x = 0; x < neurons1.size(); x++)
            for (int y = 0; y < neurons2.size(); y++)
                connect(neurons1.get(x), neurons2.get(y));
    }

    private boolean displaySort = false;

    private void printList(ArrayList<Neuron> neurons) {
        if (displaySort) {
            for (int x = 0; x < neurons.size(); x++) {
                for (int y = 0; y < neurons.get(x).inputs.size(); y++) {
                    System.out.print(getNeuronPos(neurons.get(x).inputs.get(y).n1) + " ");
                }
                System.out.print("- ");
                for (int y = 0; y < neurons.get(x).outputs.size(); y++) {
                    System.out.print(getNeuronPos(neurons.get(x).outputs.get(y).n2) + " ");
                }
                if (x < neurons.size() - 1)
                    System.out.print("___ ");
            }
            System.out.println();
        }
    }

    private void print(double[] list) {
        for (int x = 0; x < list.length; x++) {
            System.out.print(list[x] + " ");
        }
        System.out.println();
    }

    //sort the neurons so left to right; low to high is always feed forward, and other way feed backward
    public void sortNeurons() {
        ///shrug
        //error if it goes cyclic

        boolean changed;
        int count = 0;

        //printList (neurons);
        do {
            changed = false;
            count++;

            ArrayList<Neuron> neuronsClone = (ArrayList<Neuron>) neurons.clone();
            for (int n = 0; n < neuronsClone.size(); n++) {
                //push all inputs to left, all outputs to right
                Neuron center = neuronsClone.get(n);

                ArrayList<Weight> inputsClone = (ArrayList<Weight>) center.inputs.clone();
                for (int x = 0; x < inputsClone.size(); x++) {
                    Weight w = inputsClone.get(x);
                    int pos = getNeuronPos(center);
                    if (getNeuronPos(w.n1) > pos) {
                        neurons.remove(w.n1);
                        neurons.add(pos, w.n1);
                        changed = true;
                        printList(neurons);
                    }
                }

                ArrayList<Weight> outputsClone = (ArrayList<Weight>) center.outputs.clone();
                for (int x = 0; x < outputsClone.size(); x++) {
                    Weight w = outputsClone.get(x);
                    int pos = getNeuronPos(center);
                    if (getNeuronPos(w.n2) < pos) {
                        neurons.remove(w.n2);
                        neurons.add(pos, w.n2);
                        changed = true;
                        printList(neurons);
                    }
                }

            }

        } while (changed && count < neurons.size() + 1);

        if (count >= neurons.size() + 1)
            System.out.println("not sorted");
    }

    public int getNeuronPos(Neuron n) {
        for (int x = 0; x < neurons.size(); x++) {
            if (neurons.get(x) == (n)) {
                return x;
            }
        }
        return -1;
    }
}
