package cn.elytra.mod.drop_like_copper.api.utils;

import com.google.common.collect.AbstractIterator;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Function;

public class Utils {

    private Utils() {
    }

    public static <T, TT, R> Function<T, R> transformFunctionInputType(Function<TT, R> original, Function<T, TT> inputTransform) {
        return t -> original.apply(inputTransform.apply(t));
    }

    public static <T, TT> Iterator<TT> transformIteratorType(Iterator<T> iterator, Function<T, TT> transform) {
        return new AbstractIterator<>() {

            @Nullable
            @Override
            protected TT computeNext() {
                if (iterator.hasNext()) {
                    return transform.apply(iterator.next());
                }
                return endOfData();
            }
        };
    }

}
