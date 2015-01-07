package com.redhat.lightblue.client.http.testing.doubles;

import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.util.TypeLiteral;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class StubInstance<T> implements Instance<T> {
    private final T instance;

    public StubInstance(T instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        if (isAmbiguous()) {
            throw new AmbiguousResolutionException();
        }

        if (isUnsatisfied()) {
            throw new UnsatisfiedResolutionException();
        }

        return instance;
    }

    @Override
    public Instance<T> select(Annotation... qualifiers) {
        throw new UnsupportedOperationException("select");
    }

    @Override
    public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        throw new UnsupportedOperationException("select");
    }

    @Override
    public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        throw new UnsupportedOperationException("select");
    }

    @Override
    public boolean isUnsatisfied() {
        return instance == null;
    }

    @Override
    public boolean isAmbiguous() {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor == 0;
            }

            @Override
            public T next() {
                if (cursor > 0) {
                    throw new NoSuchElementException();
                }

                cursor++;

                return StubInstance.this.get();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
