package com.jaggy.Musica.tasks;

import com.jaggy.Musica.events.CommandEvent;

public interface TaskFactory {

    Task buildTask(CommandEvent commandEvent);

}
