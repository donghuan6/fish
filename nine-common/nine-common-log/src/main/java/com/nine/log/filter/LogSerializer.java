package com.nine.log.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;

import java.io.IOException;
import java.util.Set;

@Getter
public class LogSerializer extends JsonSerializer<Object> {
    private final Set<String> excludes;
    private final ObjectMapper mapper;

    public LogSerializer(Set<String> excludes) {
        this.excludes = excludes;
        // 构建带过滤器的 ObjectMapper
        mapper = JsonMapper.builder()
                .filterProvider(
                        new SimpleFilterProvider()
                                .addFilter("sysLogFilter", SimpleBeanPropertyFilter.serializeAllExcept(this.excludes)))
                .build();
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 写入过滤后的 json
        String json = mapper.writeValueAsString(value);
        gen.writeRawValue(json);
    }
}
