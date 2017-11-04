/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.sequence;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class SequenceValue {
    private String sequenceName = null;

    private long curValue = 1;
    private long maxValue = 0;

    public SequenceValue(String name) {
        sequenceName = name.toUpperCase();
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setCurValue(long curValue) {
        this.curValue = curValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public boolean hasNext() {
        return curValue <= maxValue;
    }

    public long getNextValue() {
        return curValue++;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SequenceValue)) {
            return false;
        }

        return sequenceName.equalsIgnoreCase(((SequenceValue) obj).getSequenceName());
    }

    public int hashCode() {
        return sequenceName.hashCode();
    }
}
