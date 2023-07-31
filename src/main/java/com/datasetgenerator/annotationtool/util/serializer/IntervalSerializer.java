package com.datasetgenerator.annotationtool.util.serializer;

import com.datasetgenerator.annotationtool.util.Interval;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class IntervalSerializer extends JsonSerializer<Interval> {
    @Override
    public void serialize(Interval interval, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("start", interval.getStart());
        jsonGenerator.writeNumberField("end", interval.getEnd());
        jsonGenerator.writeEndObject();
    }
}