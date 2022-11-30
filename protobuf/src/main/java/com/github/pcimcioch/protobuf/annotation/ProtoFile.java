package com.github.pcimcioch.protobuf.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents protobuf file that contains multiple protobuf structures
 */
public class ProtoFile {

    private final List<Message> messages = new ArrayList<>();
    private final Map<String, String> options = new HashMap<>();

    /**
     * Crete protobuf file from annotations
     *
     * @param options  option annotations
     * @param messages message annotations
     */
    public ProtoFile(Option[] options, Message[] messages) {
        this.messages.addAll(List.of(messages));
        for (Option option : options) {
            this.options.put(option.name(), option.value());
        }
    }

    /**
     * Returns messages
     *
     * @return messages
     */
    public List<Message> messages() {
        return messages;
    }

    /**
     * Returns whether given option is defined in this file
     *
     * @param name name of the option
     * @return whether given option is defined
     */
    public boolean hasOption(String name) {
        return getOption(name).isPresent();
    }

    /**
     * Returns given option's value
     *
     * @param name option name
     * @return optional value
     */
    public Optional<String> getOption(String name) {
        return Optional.ofNullable(options.get(name));
    }

    /**
     * Adds new option. If option with given name is already present it is overridden
     *
     * @param name  option name
     * @param value option value
     */
    public void addOption(String name, String value) {
        options.put(name, value);
    }
}
