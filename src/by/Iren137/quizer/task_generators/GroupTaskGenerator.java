package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.exceptions.EmptyGenerators;
import by.Iren137.quizer.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class GroupTaskGenerator implements TaskGenerator {
    ArrayList<Task> tasks;

    /**
     * Конструктор с переменным числом аргументов
     *
     * @param generators генераторы, которые в конструктор передаются через запятую
     */
    public GroupTaskGenerator(TaskGenerator... generators) {
        if (generators == null || generators.length == 0) {
            throw new EmptyGenerators();
        }
        for (TaskGenerator generator : generators) {
            tasks.add(generator.generate());
        }
    }

    /**
     * Конструктор, который принимает коллекцию генераторов
     *
     * @param generators генераторы, которые передаются в конструктор в Collection (например, {@link ArrayList})
     */
    GroupTaskGenerator(Collection<TaskGenerator> generators) {
        if (generators == null || generators.size() == 0) {
            throw new EmptyGenerators();
        }
        tasks = new ArrayList<>(generators.size());
        for (TaskGenerator generator : generators) {
            tasks.add(generator.generate());
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
            int num = random.nextInt() % tasks.size();
            return this.tasks.get(num);
        } else {
            throw new IllegalArgumentException("Tasks are empty");
        }
    }
}
