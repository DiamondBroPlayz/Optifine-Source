// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class CounterInt
{
    private int startValue;
    private int value;
    
    public CounterInt(final int startValue) {
        this.startValue = startValue;
        this.value = startValue;
    }
    
    public synchronized int nextValue() {
        final int valueNow = this.value;
        ++this.value;
        return valueNow;
    }
    
    public synchronized void reset() {
        this.value = this.startValue;
    }
    
    public int getValue() {
        return this.value;
    }
}
