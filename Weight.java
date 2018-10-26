package com.example.joeym.playground;

public class Weight {

    //the weight from the Neuron n to the owner of the pointer
    double w;

    Neuron n1;//goes into n2
    Neuron n2;//receives from n1

    public Weight(Neuron n1, Neuron n2) {
        this.n1 = n1;
        this.n2 = n2;
        w = Neuron.randomValue();//divided by something
    }

    //feed-forward, n1 is in front; to the left; before; the input for n2.
    public double getWeightedValue() {
        return w * n1.value;
    }

    public void backprop(double lr) {
        w = w - n2.error * n2.derivative * n1.value * lr;
    }
}