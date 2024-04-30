package org.example.service.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * Клас JSONParser надає функціональність для розбору JSON-файлів в каталозі та підрахунку входжень конкретного поля.
 * Він використовує багатопотоковість для покращення продуктивності.
 */
public class JSONParser {
    private final ExecutorService executorService;
    
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    /**
     * Конструктор класу JSONParser з вказаною початковою та максимальною кількістю потоків.
     *
     * @param initialThreadAmount Початкова кількість потоків у пулі потоків.
     * @param maxThreadAmount     Максимальна кількість потоків у пулі потоків.
     */
    public JSONParser(int initialThreadAmount, int maxThreadAmount) {
        this.executorService = new ThreadPoolExecutor(initialThreadAmount, maxThreadAmount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * Розбирає JSON-файли у вказаному каталозі та підраховує входження вказаного поля.
     *
     * @param directoryPath Шлях до каталогу з JSON-файлами.
     * @param parseField    Поле, яке слід розбирати та підраховувати входження.
     * @return Мапа, що містить розібрані значення поля та відповідні кількості входжень, відсортовані за кількістю входжень у спадаючому порядку.
     */
    public Map<String, Integer> parseJsonFiles(String directoryPath, String parseField) {
        Map<String, Integer> result = new ConcurrentHashMap<>();
        try (Stream<Path> paths = Files.list(Path.of(directoryPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> executorService.submit(() -> {
                        try {
                            parseFile(filePath.toString(), parseField, result);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            return sortByValue(result);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Розбирає JSON-файл та підраховує входження вказаного поля.
     *
     * @param file       Шлях до JSON-файлу.
     * @param parseField Поле, яке слід розбирати та підраховувати входження.
     * @param result     Мапа для зберігання розібраних значень поля та відповідних кількостей входжень.
     * @throws IOException Якщо сталася помилка вводу/виводу під час розбору JSON-файлу.
     */
    private void parseFile(String file, String parseField, Map<String, Integer> result) throws IOException {
        try (InputStream in = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(in);
             JsonParser jParser = new JsonFactory().createParser(reader)) {

            while (jParser.nextToken() != null) {
                String fieldName = jParser.getCurrentName();
                if (parseField.equals(fieldName)) {
                    jParser.nextToken();
                    jParser.nextToken();

                    String fieldValue = jParser.getText();

                    while (!Objects.equals(fieldValue, "]")) {
                        if (result.containsKey(fieldValue)) {
                            result.put(fieldValue, result.get(fieldValue) + 1);
                        } else {
                            result.put(fieldValue, 1);
                        }
                        jParser.nextToken();
                        fieldValue = jParser.getText();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Сталася помилка під час розбору JSON-файлу: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Сортує мапу за її значеннями у спадному порядку.
     *
     * @param map Мапа, яку слід відсортувати.
     * @return LinkedHashMap, що містить відсортовані записи.
     */
    private static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());

        entries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}