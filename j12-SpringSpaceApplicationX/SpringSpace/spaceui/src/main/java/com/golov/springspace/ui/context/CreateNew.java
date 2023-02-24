package com.golov.springspace.ui.context;
import com.golov.springspace.station.Reply;

@FunctionalInterface
public interface CreateNew {
    Reply create(String model);
}
