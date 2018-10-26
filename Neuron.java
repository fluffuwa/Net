package com.example.joeym.playground;

import java.util.ArrayList;

public class Neuron {
    public enum FunctionType {SIGMOID, LINEAR, RELU}

    ;

    static double randomValue() {
        return (Math.random() - 0.5);//divided by some number
    }

    public Neuron(FunctionType functionType) {
        bias = randomValue();
        this.functionType = functionType;
    }

    FunctionType functionType = FunctionType.LINEAR;

    ArrayList<Weight> inputs = new ArrayList<Weight>();
    ArrayList<Weight> outputs = new ArrayList<Weight>();
    double bias;//an input neuron that's value is always 1

    //this neuron's output value
    double value;

    //this neuron's error during backpropagation
    double error;

    double derivative;

    //use input neurons and weights to calculate this neuron's value
    //if this is an input neuron, manually edit the bias to be the neuron's value
    public void forward() {
        double tempValue = bias;
        for (int x = 0; x < inputs.size(); x++) {
            tempValue += inputs.get(x).getWeightedValue();
        }
        value = activation(tempValue);//tempValue isn't kept, so the derivative uses value, since it's been sigmoided.
    }

    //use backpropagation to update input and bias weights.
    //manually edit the error value if it's an output neuron (if it doesn't have any outputs)
    public void backward(double lr) {
        if (outputs.size() != 0) {
            double tempError = 0;
            for (int x = 0; x < outputs.size(); x++) {
                Weight outputWeight = outputs.get(x);
                tempError += (outputWeight.n2.derivative * outputWeight.w * outputWeight.n2.error);
            }
            error = tempError;
        }
        derivative = derivative(value);
        for (int x = 0; x < inputs.size(); x++) {
            inputs.get(x).backprop(lr);
        }
        bias = bias - error * derivative * lr;
    }

    public double activation(double in) {
        switch (functionType) {
            case SIGMOID:
                return (1.0 / (1.0 + Math.exp(-in)));
            case LINEAR:
                return in;
            case RELU:
                return Math.max(in, 0);
        }
        System.out.println("activation function type not found");
        return -1;
    }

    public double derivative(double in) {
        switch (functionType) {
            case SIGMOID:
                //return (activation (in) * (1.0 - activation(in))); <- this doesn't work lol.
                return ((in) * (1.0 - (in)));
            case LINEAR:
                return 1;
            case RELU:
                if (in > 0)
                    return 1;
                else
                    return 0;
        }
        System.out.println("activation function type not found");
        return -1;
    }
}