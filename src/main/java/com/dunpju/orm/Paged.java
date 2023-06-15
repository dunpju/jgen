package com.dunpju.orm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Paged<E> {
    long total;
    long current;
    long size;
    List<E> list;
}
