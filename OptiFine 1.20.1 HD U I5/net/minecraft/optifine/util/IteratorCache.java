// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Deque;

public class IteratorCache
{
    private static Deque<IteratorReusable<Object>> dequeIterators;
    
    public static Iterator<Object> getReadOnly(final List list) {
        synchronized (IteratorCache.dequeIterators) {
            IteratorReusable<Object> it = IteratorCache.dequeIterators.pollFirst();
            if (it == null) {
                it = new IteratorReadOnly();
            }
            it.setList(list);
            return it;
        }
    }
    
    private static void finished(final IteratorReusable<Object> iterator) {
        synchronized (IteratorCache.dequeIterators) {
            if (IteratorCache.dequeIterators.size() > 1000) {
                return;
            }
            iterator.setList(null);
            IteratorCache.dequeIterators.addLast(iterator);
        }
    }
    
    static {
        IteratorCache.dequeIterators = new ArrayDeque<IteratorReusable<Object>>();
        for (int i = 0; i < 1000; ++i) {
            final IteratorReadOnly it = new IteratorReadOnly();
            IteratorCache.dequeIterators.add(it);
        }
    }
    
    public static class IteratorReadOnly implements IteratorReusable<Object>
    {
        private List<Object> list;
        private int index;
        private boolean hasNext;
        
        @Override
        public void setList(final List<Object> list) {
            if (this.hasNext) {
                throw new RuntimeException(invokedynamic(makeConcatWithConstants:(Ljava/util/List;Ljava/util/List;)Ljava/lang/String;, this.list, list));
            }
            this.list = list;
            this.index = 0;
            this.hasNext = (list != null && this.index < list.size());
        }
        
        @Override
        public Object next() {
            if (!this.hasNext) {
                return null;
            }
            final Object obj = this.list.get(this.index);
            ++this.index;
            this.hasNext = (this.index < this.list.size());
            return obj;
        }
        
        @Override
        public boolean hasNext() {
            if (!this.hasNext) {
                IteratorCache.finished(this);
                return false;
            }
            return this.hasNext;
        }
    }
    
    public interface IteratorReusable<E> extends Iterator<E>
    {
        void setList(final List<E> p0);
    }
}
