package za.co.knox.restservice.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Utils {

    public static int generateRandomNumbers( int start, int end) {
        Random rand = new Random();

        return rand.nextInt(start + end);

    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public static Throwable getRootCause(Throwable e) {
        if (e.getCause() == null) return e;
        return getRootCause(e.getCause());
    }

    public static Throwable getRootException(Throwable exception){
        Throwable rootException=exception;
        while(rootException.getCause()!=null){
            rootException = rootException.getCause();
        }
        return rootException;
    }


    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> LoadList(String fileName, Class<T> valueType) {
        ObjectMapper objectMapper = getObjectMapper();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, valueType);
            List<T> objectList = (List) objectMapper.readValue(classloader.getResourceAsStream(fileName), listType);
            return objectList;
        } catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) {
        T data = null;

        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
        }
        return data;
    }
}
