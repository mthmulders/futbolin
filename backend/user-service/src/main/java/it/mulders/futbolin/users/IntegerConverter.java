package it.mulders.futbolin.users;

import org.eclipse.microprofile.config.spi.Converter;

public class IntegerConverter implements Converter<Integer> {
    @Override
    public Integer convert(String value) {
        return Integer.valueOf(value);
    }
}
