package com.example.joeym.playground;

public class QA {
    public double[] realInputs;
    public double[] realOutputs;

    public QA(int test) {
        switch (test) {
            case 1:
                initialize(1, 1);
                realOutputs[0] = 1 - realInputs[0];
                break;
            case 2:
                initialize(2, 2);
                realOutputs[0] = realInputs[0] > 0.5 ? 1 : 0;
                realOutputs[1] = realInputs[1] > 0.5 ? 1 : 0;
                break;
            case 3:
                initialize(2, 1);
                realInputs[0] = realInputs[0] > 0.5 ? 1 : 0;
                realInputs[1] = realInputs[1] > 0.5 ? 1 : 0;
                realOutputs[0] = (realInputs[0] == realInputs[1]) ? 0 : 1;
                break;
            case 4:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 50.0 - 25.0;//range of x
                realInputs[1] = realInputs[1] * 50.0 - 25.0;//range of y
                double x = realInputs[0];
                realOutputs[0] = (realInputs[1] > x * x) ? 1 : 0;
                break;
            case 5:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 5.0 - 1.0;//range of x
                realInputs[1] = realInputs[1] * 5.0 - 2.5;//range of y
                double y = realInputs[0];
                realOutputs[0] = (realInputs[1] > Math.log(y)) ? 1 : 0;
                break;
            case 6:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 50.0 - 25.0;//range of x
                realInputs[1] = realInputs[1] * 50.0 - 25.0;//range of y
                double z = realInputs[0];
                realOutputs[0] = (realInputs[1] > 2.0 * z) ? 1 : 0;
                break;
            case 7:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 5.0 - 2.5;//range of x
                realInputs[1] = realInputs[1] * 5.0 - 2.5;//range of y
                double a = realInputs[0];
                realOutputs[0] = (realInputs[1] > a * a * a) ? 1 : 0;
                break;
            case 8:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 5.0 - 2.5;//range of x
                realInputs[1] = realInputs[1] * 5.0 - 2.5;//range of y
                double b = realInputs[0];
                realOutputs[0] = (realInputs[1] > b * b * b * b * b) ? 1 : 0;
                break;
            case 9:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 10.0 - 5.0;//range of x
                realInputs[1] = realInputs[1] * 10.0 - 5.0;//range of y
                double c = realInputs[0];
                realOutputs[0] = (realInputs[1] > Math.exp(c)) ? 1 : 0;
                break;
            case 10:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 10.0 - 5.0;//range of x
                realInputs[1] = realInputs[1] * 10.0 - 5.0;//range of y
                double d = realInputs[0];
                realOutputs[0] = ((realInputs[1] > 0) != (realInputs[1] > d)) ? 1 : 0;
                break;
            case 11:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 6.0 - 3.0;//range of x
                realInputs[1] = realInputs[1] * 6.0 - 3.0;//range of y
                double e = realInputs[0];
                realOutputs[0] = ((realInputs[1] > 0) != (realInputs[1] > e) != (realInputs[1] > e * e - 2)) ? 1 : 0;
                break;
            case 12:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 2.0 - 1.0;//range of x
                realInputs[1] = realInputs[1] * 2.0 - 1.0;//range of y
                double f = realInputs[0];
                realOutputs[0] = ((realInputs[1] > -0.3 != realInputs[1] > +0.3 !=
                        realInputs[1] > f - 0.3 != realInputs[1] > f + 0.3)) ? 1 : 0;
                break;

        }

    }

    public void initialize(int size1, int size2) {
        realInputs = new double[size1];
        for (int x = 0; x < size1; x++) {
            realInputs[x] = Math.random();
        }
        realOutputs = new double[size2];
    }
}
