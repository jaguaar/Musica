package com.jaggy.Musica.events;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.dv8tion.jda.api.entities.Message;

public class CommandEvent {

    private final Message message;

    private final String action;
    private final List<String> args;

    public CommandEvent(Message message, String action, List<String> args) {
        this.message = message;
        this.action = action;
        this.args = args;
    }

    public String getAction() {
        return action;
    }

    public List<String> getArgs() {
        return args;
    }

    public Message getMessage() {
        return message;
    }

    public String getAuthorId(){
        return this.message.getAuthor().getId();
    }


}
