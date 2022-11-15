//TODO Посмотреть внимательней реализацию ObjectMapper
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//public class Parser {
//
//    private final ObjectMapper mapper;
//    private final String delimiter;
//
//    public Parser(String delimiter) {
//        this.mapper = new ObjectMapper();
//        this.delimiter = delimiter;
//    }
//
//    public <T> List<T> read(String pathFile, Class<T> clazz) {
//        try (final Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(pathFile)))) {
//            final List<T> valuesList = new ArrayList<>();
//            final String[] title = scanner.nextLine().split(delimiter);
//            while (scanner.hasNext()) {
//                final String[] value = scanner.nextLine().split(delimiter);
//                final Map<String, Object> values = IntStream.range(0, value.length)
//                        .boxed()
//                        .filter(i -> value[i] != null && !value[i].trim().isEmpty())
//                        .collect(Collectors.toMap(k -> title[k], v -> value[v]));
//                valuesList.add(mapper.convertValue(values, clazz));
//            }
//            return valuesList;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}