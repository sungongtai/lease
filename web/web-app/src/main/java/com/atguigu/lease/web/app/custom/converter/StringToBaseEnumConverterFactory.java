package com.atguigu.lease.web.app.custom.converter;

import com.atguigu.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String source) {
                T[] enumConstants = targetType.getEnumConstants();
                for (T t :enumConstants){
                    if(t.getCode().equals(Integer.valueOf(source))){
                        return t;
                    }
                }
                throw new IllegalArgumentException("No enum constant " + targetType.getCanonicalName() + "." + source);
            }
        };
    }
}
