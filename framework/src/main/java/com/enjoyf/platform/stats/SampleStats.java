/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.stats;

/**
 * A class for doing statistics on a set of samples.
 */
public class SampleStats {
    /**
     * Ctor just sets things up.
     */
    public SampleStats() {
        reset();
    }

    /**
     * Reset the object to have no samples.
     */
    public void reset() {
        m_n = 0;
        m_x = 0.0;
        m_x2 = 0.0;
        m_maxValue = Double.MIN_VALUE;
        m_minValue = Double.MAX_VALUE;
    }

    /**
     * Add a sample. Call this routine several times to build up
     * your sample space. Then, call the other routines to
     * retrieve info about your sample space.
     */
    public void add(double value) {
        m_n += 1;
        m_x += value;
        m_x2 += (value * value);
        if (m_minValue > value) {
            m_minValue = value;
        }
        if (m_maxValue < value) {
            m_maxValue = value;
        }
    }

    /**
     * Return the no. of samples.
     */
    public int count() {
        return m_n;
    }

    /**
     * Return min value.
     */
    public double minval() {
        return m_minValue;
    }

    /**
     * Return max value.
     */
    public double maxval() {
        return m_maxValue;
    }

    /**
     * Return the mean.
     */
    public double mean() {
        if (m_n > 0) {
            return (m_x / m_n);
        } else {
            return 0.0;
        }
    }

    /**
     * Return the variance.
     */
    public double var() {
        if (m_n > 1) {
            return ((m_x2 - ((m_x * m_x) / m_n)) / (m_n - 1));
        } else {
            return 0.0;
        }
    }

    /**
     * Return the std deviation.
     */
    public double stdDev() {
        if (m_n <= 0 || var() <= 0) {
            return 0.0;
        } else {
            return Math.sqrt(var());
        }
    }

    /**
     * Return the confidence.
     *
     * @param interval Not sure what this is all about.
     */
    public double confidence(int interval) {
        int df = m_n - 1;
        if (df <= 0) {
            return Double.MAX_VALUE;
        }

        double t = p_tval((double) (100 + interval) * 0.005, df);
        if (t == Double.MAX_VALUE) {
            return t;
        } else {
            return (t * stdDev()) / Math.sqrt((double) m_n);
        }
    }

    /**
     * Return the confidence.
     *
     * @param pvalue Not sure what this is all about.
     */
    public double confidence(double pvalue) {
        int df = m_n - 1;
        if (df <= 0) {
            return Double.MAX_VALUE;
        }
        double t = p_tval((1.0 + pvalue) * 0.5, df);
        if (t == Double.MAX_VALUE) {
            return t;
        } else {
            return (t * stdDev()) / Math.sqrt((double) m_n);
        }
    }

    public boolean isOutsideStddev(double val) {
        return Math.abs(val - mean()) > stdDev();
    }

    /**
     * t-distribution: given p-value and degrees of freedom, return
     * t-value. Adapted from Peizer & Pratt JASA, vol63, p1416
     */
    private double p_tval(double p, int df) {
        double t;
        boolean positive = (p >= 0.5);
        p = (positive) ? 1.0 - p : p;
        if (p <= 0.0 || df <= 0) {
            t = Double.MAX_VALUE;
        } else if (p == 0.5) {
            t = 0.0;
        } else if (df == 1) {
            t = 1.0 / Math.tan((p + p) * 1.57079633);
        } else if (df == 2) {
            t = Math.sqrt(1.0 / ((p + p) * (1.0 - p)) - 2.0);
        } else {
            double ddf = df;
            double a = Math.sqrt(Math.log(1.0 / (p * p)));
            double aa = a * a;
            a = a - ((2.515517 + (0.802853 * a) + (0.010328 * aa))
                    / (1.0 + (1.432788 * a) + (0.189269 * aa) +
                    (0.001308 * aa * a)));
            t = ddf - 0.666666667 + 1.0 / (10.0 * ddf);
            t = Math.sqrt(ddf * (Math.exp(
                    a * a * (ddf - 0.833333333) / (t * t)) - 1.0));
        }
        return (positive) ? t : -t;
    }

    protected int m_n;
    protected double m_x;
    protected double m_x2;
    protected double m_minValue;
    protected double m_maxValue;
}
