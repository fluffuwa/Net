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
            case 13:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 2.0 - 1.0;//range of x
                realInputs[1] = realInputs[1] * 2.0 - 1.0;//range of y
                realOutputs[0] = (realInputs[0] * realInputs[0] + realInputs[1] * realInputs[1] > 0.5) ? 1 : 0;
                break;
            case 14:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 2.0 - 1.0;//range of x
                realInputs[1] = realInputs[1] * 2.0 - 1.0;//range of y
                realOutputs[0] = (realInputs[0] * realInputs[0] + realInputs[1] * realInputs[1] > 0.8 !=
                        realInputs[0] * realInputs[0] + realInputs[1] * realInputs[1] > 0.3) ? 1 : 0;
                break;
            case 15:
                initialize(2, 1);
                realInputs[0] = realInputs[0] * 4.0 - 2.0;//range of x
                realInputs[1] = realInputs[1] * 4.0 - 2.0;//range of y
                double g = realInputs[0];
                double h = realInputs[1];
                realOutputs[0] = ((g + 1) * (g + 1) + (h) * (h) < 1 ||
                        ((g - 1) * (g - 1) + h * h < 1)
                ) ? 1 : 0;
                break;
            case 16:
                initialize(2, 1);
                double i = realInputs[0];
                double j = realInputs[1];
                realOutputs[0] = (i > 0.25 && i < 0.75) ? (
                        (
                                (j > 0.60 && j < 0.80 && i < 0.50)
                                        || (j > 0.20 && j < 0.40 && i > 0.50)
                        ) ? 0 : 1
                ) : 0;
                break;
            case 17:
                initialize(2, 1);
                realInputs[0] = realInputs[0];
                realInputs[1] = realInputs[1];
                realOutputs[0] = (realInputs[0] > 0.25 && realInputs[0] < 0.75) ? (
                        (realInputs[1] < 0.25 || realInputs[1] > 0.75) ? 1 : 0
                ) : 0;
                break;
            case 18:
                initialize(2, 1);
                double k = realInputs[0] * 100;
                double l = realInputs[1] * 100;
                realOutputs[0] = (Math.sqrt((k - 62.5) * (k - 62.5) + (l - 62.5) * (l - 62.5)) > 37.5 != Math.sqrt((k - 25) * (k - 25) + (l - 25) * (l - 25)) > 12.5) ? 1 : 0;
                break;
            case 19:
                initialize(2, 5);
                realOutputs[0] = realInputs[0];
                realOutputs[1] = realInputs[0] + realInputs[1];
                realOutputs[2] = realInputs[0] * realInputs[1];
                realOutputs[3] = realInputs[0] - realInputs[1];
                realOutputs[4] = Math.sqrt((realInputs[0] - 0.5) * (realInputs[0] - 0.5) + (realInputs[1] - 0.5) * (realInputs[1] - 0.5)) > 0.25 ? 1 : 0;
                break;
            default://funky town
                System.out.println("QA " + test + " not found.");
                int[] twoones = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
                int testCase = (int) Math.floor(Math.random() * twoones.length);
                QA qa = new QA(testCase);
                initialize(3, 1);
                realInputs[0] = qa.realInputs[0];
                realInputs[1] = qa.realInputs[1];
                realInputs[2] = testCase;
                realOutputs[0] = qa.realOutputs[0];
                break;
        }

    }

    static int maxQA = 19;

    public void initialize(int size1, int size2) {
        realInputs = new double[size1];
        for (int x = 0; x < size1; x++) {
            realInputs[x] = Math.random();
        }
        realOutputs = new double[size2];
    }
}
