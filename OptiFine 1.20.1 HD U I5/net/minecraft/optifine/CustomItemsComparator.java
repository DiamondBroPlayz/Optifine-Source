// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Comparator;

public class CustomItemsComparator implements Comparator
{
    @Override
    public int compare(final Object o1, final Object o2) {
        final CustomItemProperties p1 = (CustomItemProperties)o1;
        final CustomItemProperties p2 = (CustomItemProperties)o2;
        if (p1.weight != p2.weight) {
            return p2.weight - p1.weight;
        }
        if (!Config.equals(p1.basePath, p2.basePath)) {
            return p1.basePath.compareTo(p2.basePath);
        }
        return p1.name.compareTo(p2.name);
    }
}
