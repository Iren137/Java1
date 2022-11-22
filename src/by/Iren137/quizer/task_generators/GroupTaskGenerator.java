package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.exceptions.EmptyGenerators;
import by.Iren137.quizer.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static java.lang.Math.abs;

public class GroupTaskGenerator implements Task.Generator {
    ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Конструктор с переменным числом аргументов
     *
     * @param generators генераторы, которые в конструктор передаются через запятую
     */
    public GroupTaskGenerator(Task.Generator... generators) {
        if (generators == null || generators.length == 0) {
            throw new EmptyGenerators();
        }
        for (Task.Generator generator : generators) {
            this.tasks.add(generator.generate());
        }
    }

    /**
     * Конструктор, который принимает коллекцию генераторов
     *
     * @param generators генераторы, которые передаются в конструктор в Collection (например, {@link ArrayList})
     */
    public GroupTaskGenerator(Collection<Task.Generator> generators) {
        if (generators == null || generators.size() == 0) {
            throw new EmptyGenerators();
        }
        this.tasks = new ArrayList<>(generators.size());
        for (Task.Generator generator : generators) {
            this.tasks.add(generator.generate());
        }
    }

    /**
     * @return результат метода generate() случайного генератора из списка.
     * Если этот генератор выбросил исключение в методе generate(), выбирается другой.
     * Если все генераторы выбрасывают исключение, то и тут выбрасывается исключение.
     */
    @Override
    public Task generate() {
        if (this.tasks.size() > 0) {
            final Random random = new Random();
            int num = abs(random.nextInt()) % this.tasks.size();
            return this.tasks.get(num);
        } else {
            throw new IllegalArgumentException("Tasks are empty");
        }
    }
}
