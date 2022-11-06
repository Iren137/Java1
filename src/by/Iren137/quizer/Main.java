package by.Iren137.quizer;

import by.Iren137.quizer.quiz.*;
import by.Iren137.quizer.task_generators.*;
import by.Iren137.quizer.tasks.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Map<String, Quiz> quizMap = getQuizMap();
        boolean is_quiz = false;
        Quiz test = null;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Write name of the test\n");
            String testName = in.toString();
            for (Map.Entry<String, Quiz> element : quizMap.entrySet()) {
                if (Objects.equals(element.getKey(), testName)) {
                    is_quiz = true;
                    test = element.getValue();
                    break;
                } else {
                    System.out.println("Wrong name. Try again\n");
                }
            }
        } while (!is_quiz);

        while (!test.isFinished()) {
            System.out.println(test.nextTask().getText());
            String answer = in.toString();
            Result result = test.provideAnswer(answer);
            switch (result) {
                case OK -> {
                    System.out.println("Right!");
                }
                case WRONG -> {
                    System.out.println("Wrong!!!");
                }
                case INCORRECT_INPUT -> {
                    System.out.println("Incorrect input. Try again");
                }
            }
        }
        System.out.printf("Вaш результат: %f\n", test.getMark());
    }

    /**
     * @return тесты в {@link Map}, где
     * ключ     - название теста {@link String}
     * значение - сам тест       {@link Quiz}
     */
    static Map<String, Quiz> getQuizMap() throws IOException, ParseException {
        HashMap<String, Quiz> result = new HashMap<>();
        JSONParser parser = new JSONParser();
        JSONArray quizArray = (JSONArray) parser.parse(new FileReader("src/by/Iren137/quizer/input.json"));
        for (Object quizObj : quizArray) {
            JSONObject quiz = (JSONObject) quizObj;
            String name = (String) quiz.get("name");
            int taskCount = (int) (long) quiz.get("number");
            String generator = (String) quiz.get("generator");
            try {
                switch (generator) {
                    case "TextTaskGenerator" -> result.put(name, new Quiz(getTextTaskGenerator(quiz), taskCount));
                    case "ExpressionTaskGenerator" ->
                            result.put(name, new Quiz(getExpressionTaskGenerator(quiz), taskCount));
                    case "EquationTaskGenerator" ->
                            result.put(name, new Quiz(getEquationTaskGenerator(quiz), taskCount));
                    case "GroupTaskGenerator" -> result.put(name, new Quiz(getGroupTaskGenerator(quiz), taskCount));
                    case "PoolTaskGenerator" -> result.put(name, new Quiz(getPoolTaskGenerator(quiz), taskCount));
                    default -> throw new IllegalClassFormatException("Unknown type of generator");
                }
            } catch (IllegalArgumentException | IllegalClassFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static TextTaskGenerator getTextTaskGenerator(JSONObject jsonObject) {
        try {
            String taskText = (String) jsonObject.get("text");
            String taskAnswer = (String) jsonObject.get("answer");
            return new TextTaskGenerator(taskText, taskAnswer);
        } catch (ClassCastException ignored) {
        }
        throw new IllegalArgumentException("Invalid answer");
    }

    private static Task getTextTask(JSONObject jsonObject) {
        try {
            String taskText = (String) jsonObject.get("text");
            String taskAnswer = (String) jsonObject.get("answer");
            return new TextTaskGenerator(taskText, taskAnswer).generate();
        } catch (ClassCastException ignored) {
        }
        throw new IllegalArgumentException("Invalid answer");
    }

    private static ExpressionTaskGenerator getExpressionTaskGenerator(JSONObject jsonObject) {
        int min = (int) (long) jsonObject.get("min");
        int max = (int) (long) jsonObject.get("max");
        boolean plus = jsonObject.get("plus").equals("yes");
        boolean minus = jsonObject.get("minus").equals("yes");
        boolean mul = jsonObject.get("mul").equals("yes");
        boolean div = jsonObject.get("div").equals("yes");
        return new ExpressionTaskGenerator(min, max, plus, minus, mul, div);
    }

    private static Task getExpressionTask(JSONObject jsonObject) {
        int min = (int) (long) jsonObject.get("min");
        int max = (int) (long) jsonObject.get("max");
        boolean plus = jsonObject.get("plus").equals("yes");
        boolean minus = jsonObject.get("minus").equals("yes");
        boolean mul = jsonObject.get("mul").equals("yes");
        boolean div = jsonObject.get("div").equals("yes");
        return new ExpressionTaskGenerator(min, max, plus, minus, mul, div).generate();
    }

    private static EquationTaskGenerator getEquationTaskGenerator(JSONObject jsonObject) {
        int min = (int) (long) jsonObject.get("min");
        int max = (int) (long) jsonObject.get("max");
        boolean plus = jsonObject.get("plus").equals("yes");
        boolean minus = jsonObject.get("minus").equals("yes");
        boolean mul = jsonObject.get("mul").equals("yes");
        boolean div = jsonObject.get("div").equals("yes");
        return new EquationTaskGenerator(min, max, plus, minus, mul, div);
    }

    private static Task getEquationTask(JSONObject jsonObject) {
        int min = (int) (long) jsonObject.get("min");
        int max = (int) (long) jsonObject.get("max");
        boolean plus = jsonObject.get("plus").equals("yes");
        boolean minus = jsonObject.get("minus").equals("yes");
        boolean mul = jsonObject.get("mul").equals("yes");
        boolean div = jsonObject.get("div").equals("yes");
        return new EquationTaskGenerator(min, max, plus, minus, mul, div).generate();
    }

    private static GroupTaskGenerator getGroupTaskGenerator(JSONObject jsonObject) {
        Object[] generatorsObjectArray = ((JSONArray) jsonObject.get("Generators")).toArray();
        TaskGenerator[] generators = new TaskGenerator[generatorsObjectArray.length];
        for (int i = 0; i < generatorsObjectArray.length; ++i) {
            JSONObject generator = (JSONObject) generatorsObjectArray[i];
            switch ((String) generator.get("GeneratorType")) {
                case "TextTaskGenerator" -> generators[i] = getTextTaskGenerator(generator);
                case "ExpressionTaskGenerator" -> generators[i] = getExpressionTaskGenerator(generator);
                case "EquationTaskGenerator" -> generators[i] = getEquationTaskGenerator(generator);
                case "GroupTaskGenerator" -> generators[i] = getGroupTaskGenerator(generator);
                case "PoolTaskGenerator" -> generators[i] = getPoolTaskGenerator(generator);
                default -> throw new IllegalArgumentException("No such type");
            }
        }
        return new GroupTaskGenerator(generators);
    }

    private static PoolTaskGenerator getPoolTaskGenerator(JSONObject quiz) {
        boolean allowDuplicate = (boolean) quiz.get("AllowDuplicate");
        Object[] tasksObjectArray = ((JSONArray) quiz.get("Tasks")).toArray();
        Task[] tasks = new Task[tasksObjectArray.length];
        for (int i = 0; i < tasksObjectArray.length; ++i) {
            JSONObject task = (JSONObject) tasksObjectArray[i];
            switch ((String) task.get("type")) {
                case "TextTask" -> tasks[i] = getTextTask(task);
                case "ExpressionTask" -> tasks[i] = getExpressionTask(task);
                case "EquationTask" -> tasks[i] = getEquationTask(task);
                default -> throw new IllegalArgumentException("No such type");
            }
        }
        return new PoolTaskGenerator(allowDuplicate, tasks);
    }
}