package com.bookstore.gameinfohud.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.bookstore.gameinfohud.Alignment;
import com.bookstore.gameinfohud.value.Value;

public interface IParser {

    boolean load(InputStream inputStream);

    boolean parse(Map<Alignment, List<List<Value>>> format);
}
