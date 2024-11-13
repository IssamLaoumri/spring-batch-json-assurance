package com.laoumri.miniprojetspringbatch.services;

public interface RemboursementStrategy<T> {
    double calculate(T item);
}
