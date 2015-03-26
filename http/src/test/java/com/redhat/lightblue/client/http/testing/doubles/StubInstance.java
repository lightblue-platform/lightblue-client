/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
