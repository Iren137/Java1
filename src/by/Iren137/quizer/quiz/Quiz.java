package by.Iren137.quizer.quiz;

import by.Iren137.quizer.exceptions.EmptyTasksException;
import by.Iren137.quizer.exceptions.NotFinishedException;
import by.Iren137.quizer.tasks.Task;

/**
 * Class, который описывает один тест
 */
public class Quiz {
    Task.Generator generator;
    int taskCount;
    int wrongAnswers = 0;
    int correctAnswers = 0;
    int incorrectInput = 0;
    Task task;
    boolean isIncorrect = false;

    /**
     * @param generator_in генератор заданий
     * @param taskCount_in количество заданий в тесте
     */
    public Quiz(Task.Generator generator_in, int taskCount_in) {
        this.generator = generator_in;
        this.taskCount = taskCount_in;
    }

    /**
     * @return задание, повторный вызов вернет слелующее
     * @see Task
     */
    public Task nextTask() {
        if (!isIncorrect) {
            task = generator.generate();
        }
        return task;
    }

    /**
     * Предоставить ответ ученика. Если результат {@link Result#INCORRECT_INPUT}, то счетчик неправильных
     * ответов не увеличивается, а {@link #nextTask()} в следующий раз вернет тот же самый объект {@link Task}.
     */
    public Result provideAnswer(String answer) {
        Result result;
        result = task.validate(answer);
        switch (result) {
            case OK -> {
                correctAnswers++;
                isIncorrect = false;
            }
            case WRONG -> {
                wrongAnswers++;
                isIncorrect = false;
            }
            case INCORRECT_INPUT -> {
                isIncorrect = true;
                ++incorrectInput;
            }
        }
        return result;
    }

    /**
     * @return завершен ли тест
     */
    public boolean isFinished() {
        return wrongAnswers + correctAnswers == taskCount;
    }

    /**
     * @return количество правильных ответов
     */
    int getCorrectAnswerNumber() {
        return correctAnswers;
    }

    /**
     * @return количество неправильных ответов
     */
    int getWrongAnswerNumber() {
        return wrongAnswers;
    }

    /**
     * @return количество раз, когда был предоставлен неправильный ввод
     */
    int getIncorrectInputNumber() {
        return incorrectInput;
    }

    /**
     * @return оценка, которая является отношением количества правильных ответов к количеству всех вопросов.
     * Оценка выставляется только в конце!
     */
    public double getMark() {
        if (!isFinished()) {
            throw new NotFinishedException();
        }
        if (taskCount == 0) {
            throw new EmptyTasksException();
        }
        return correctAnswers * 1.0 / taskCount;
    }
}
