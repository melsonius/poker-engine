package org.encog.engine.network.activation;

public class ActivationRectifiedLinear implements ActivationFunction {
    @Override
    public void activationFunction(double[] x, int start, int size) {
        for (int i = start; i < start + size; i++) {
//            x[i] = BoundMath.log(1 + BoundMath.exp(x[i]));
            x[i] = x[i] > 0.0 ? x[i] : 0.0;
        }
    }

    @Override
    public double derivativeFunction(double b, double a) {
//        return 1.0 / (1.0 + BoundMath.exp(-b));
        return b <= 0 ? 0 : 1;
    }

    @Override
    public boolean hasDerivative() {
        return true;
    }

    @Override
    public double[] getParams() {
        return new double[0];
    }

    @Override
    public void setParam(int index, double value) {
        // none
    }

    @Override
    public String[] getParamNames() {
        return new String[0];
    }

    @Override
    public ActivationFunction clone() {
        return new ActivationRectifiedLinear();
    }

    @Override
    public String getFactoryCode() {
        return "rectified.linear";
    }
}
