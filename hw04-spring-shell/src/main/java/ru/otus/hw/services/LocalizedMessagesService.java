package ru.otus.hw.services;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
