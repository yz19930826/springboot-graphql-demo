package com.graphqljava.tutorial.bookdetails;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GraphQLDataFetchers {

    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1",
                    "storeId", "store-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2",
                    "storeId", "store-1"),

            ImmutableMap.of("id", "book-3",
                    "name", "凯恩斯大帝的崛起",
                    "pageCount", "371",
                    "authorId", "author-3",
                    "storeId", "store-1")
    );

    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "凯恩斯大帝",
                    "lastName", "张三")
    );

    private static List<Map<String, Object>> stores = Arrays.asList(
            ImmutableMap.of("id", "store-1",
                    "name", "新华书店"
            )
    );


    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return authors
                    .stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getStoreFetcher() {
        return dataFetchingEnvironment -> {
            String storeId = dataFetchingEnvironment.getArgument("id");
            return stores
                    .stream()
                    .filter(store -> store.get("id").equals(storeId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getBookByStore() {
        return environment -> {
            Map<String, Object> store = environment.getSource();
            String storeId = (String) store.get("id");
            List<String> booksId = (List<String>) store.get("booksId");
            List<Map<String, String>> books = GraphQLDataFetchers.books
                    .stream()
                    .filter(book -> book.get("storeId").equalsIgnoreCase(storeId)).collect(Collectors.toList());
            return books;
        };
    }
}
