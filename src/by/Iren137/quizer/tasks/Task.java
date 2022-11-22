package by.Iren137.quizer.tasks;

import by.Iren137.quizer.quiz.Result;

public interface Task {
    /*
     @return текст задания
     */
    String getText();

    interface Generator {
        /**
         * Генерирует Task.
         *
         * @return задание
         * @see Task
         */
        Task generate();
    }

    /*
     * Проверяет ответ на задание и возвращает результат
     *
     * @param  answer ответ на задание
     * @return        результат ответа
     * @see           Result
     */
    Result validate(String answer);
}
