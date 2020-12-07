package com.example.testbot.commands;

import com.vk.api.sdk.objects.messages.Message;

public abstract class Command {

    private final String name;


    Command(String name) {
        this.name = name;
    }


    /**
     * Метод, который будет вызываться для исполнения команды
     *
     * @param message сообщение пользователя
     */
    public abstract void exec(Message message);


    /**
     * Метод, проверяющий, является ли сообщение пользователя вызовом данной команды
     *
     * @param message сообщение пользователя
     * @return вызвана ли команда
     */
    public boolean check(String message) {
        return name.equals(message.split(" ")[0]);
    }


    /**
     * Возвращает строку в формате:<br>
     * name: имяКоманды<br>
     *
     * @return форматированное имя и мод команды
     */
    @Override
    public String toString() {
        return String.format("name: %s", this.name);
    }


    /**
     * Берет хэш-код значащего поля {@link #name}
     *
     * @return хэш-код команды
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }


    /**
     * Объекты эквивалентны только, если поля <code>{@link #name}</code> равны
     * имеют одинаковое значение и объект является классом-наследником {@link Command}
     *
     * @param obj сравниваемый объект
     * @return {@code true} если объекты эквивалентны; {@code false} если объекты различаются
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Command) {
            return name.equals(((Command) obj).name);
        }
        return false;
    }

}