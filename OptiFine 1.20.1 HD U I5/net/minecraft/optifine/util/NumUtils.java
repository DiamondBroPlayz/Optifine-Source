// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class NumUtils
{
    public static float limit(final float val, final float min, final float max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
    
    public static int mod(final int x, final int y) {
        int result = x % y;
        if (result < 0) {
            result += y;
        }
        return result;
    }
}
