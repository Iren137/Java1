package by.Iren137.quizer;

import by.Iren137.quizer.quiz.*;
import by.Iren137.quizer.task_generators.GroupTaskGenerator;
import by.Iren137.quizer.task_generators.PoolTaskGenerator;
import by.Iren137.quizer.tasks.Task;
import by.Iren137.quizer.tasks.TextTask;
import by.Iren137.quizer.tasks.math_tasks.EquationTask;
import by.Iren137.quizer.tasks.math_tasks.ExpressionTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.instrument.IllegalClassFormatException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static by.Iren137.quizer.Constants.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Map<String, Quiz> quizMap = getQuizMap();
        System.out.println(ANSI_PURPLE + "Write name of the test" + ANSI_WHITE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String variant = reader.readLine();
        while (variant.equals("") || !quizMap.containsKey(variant)) {
            System.out.println(ANSI_RED + "Wrong name. Try again" + ANSI_WHITE);
            variant = reader.readLine();
        }
        Quiz currentQuiz = quizMap.get(variant);
        while (!currentQuiz.isFinished()) {
            System.out.print(currentQuiz.nextTask().getText());
            String answer = reader.readLine();
            Result result = currentQuiz.provideAnswer(answer);
            switch (result) {
                case OK -> System.out.println(ANSI_CYAN + "Right!" + ANSI_WHITE);
                case WRONG -> System.out.println(ANSI_RED + "Wrong!!!" + ANSI_WHITE);
                case INCORRECT_INPUT -> System.out.println(ANSI_YELLOW + "Incorrect input. Try again" + ANSI_WHITE);
            }
        }
        NumberFormat nf = new DecimalFormat("#.######");
        System.out.print(ANSI_BLUE + "Your score: " + ANSI_WHITE);
        System.out.println(ANSI_GREEN + nf.format(currentQuiz.getMark()) + ANSI_WHITE);
    }

    /**
     * @return тесты в {@link Map}, где
     * ключ     - название теста {@link String}
     * значение - сам тест       {@link Quiz}
     */
    static Map<String, Quiz> getQuizMap() throws IOException, ParseException {
        HashMap<String, Quiz> result = new HashMap<>();
        JSONParser parser = new JSONParser();
        JSONArray quizArray = (JSONArray) parser.parse(new FileReader(jsonWay));
        for (Object quizObj : quizArray) {
            JSONObject quiz = (JSONObject) quizObj;
            String name = (String) quiz.get("name");
            int taskCount = (int) (long) quiz.get("number");
            String string_generator = (String) quiz.get("generator");
            Generators generator = switch (string_generator) {
                case "EquationTaskGenerator" -> Generators.EquationGenerator;
                case "ExpressionTaskGenerator" -> Generators.ExpressionGenerator;
                case "TextTaskGenerator" -> Generators.TextGenerator;
                case "GroupTaskGenerator" -> Generators.GroupGenerator;
                case "PoolTaskGenerator" -> Generators.PoolGenerator;
                default -> Generators.Error;
            };
            boolean allowDuplicates = true;
            if (generator == Generators.PoolGenerator) {
                String duplicates = (String) quiz.get("duplication");
                allowDuplicates = duplicates.equals("yes");
            }
            JSONArray tasks = (JSONArray) ((JSONObject) quizObj).get("tasks");
            try {
                switch (generator) {
                    case TextGenerator -> result.put(name, new Quiz(getTextTaskGenerator(tasks), taskCount));
                    case ExpressionGenerator ->
                            result.put(name, new Quiz(getExpressionTaskGenerator(tasks), taskCount));
                    case EquationGenerator -> result.put(name, new Quiz(getEquationTaskGenerator(tasks), taskCount));
                    case GroupGenerator -> result.put(name, new Quiz(getGroupTaskGenerator(tasks), taskCount));
                    case PoolGenerator ->
                            result.put(name, new Quiz(getPoolTaskGenerator(tasks, allowDuplicates), taskCount));
                    default -> throw new IllegalClassFormatException("Unknown type of generator");
                }
            } catch (IllegalArgumentException | IllegalClassFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static TextTask.Generator getTextTaskGenerator(JSONArray quiz) {
        try {
            TextTask.Generator out = new TextTask.Generator();
            for (Object obj : quiz) {
                JSONObject o = (JSONObject) obj;
                out.Add((String) o.get("task"), (String) o.get("answer"));
            }
            return out;
        } catch (ClassCastException ignored) {
        }
        throw new IllegalArgumentException("Invalid answer");
    }

    private static Task getTextTask(JSONObject jsonObject) {
        try {
            String taskText = (String) jsonObject.get("task");
            String taskAnswer = (String) jsonObject.get("answer");
            return new TextTask(taskText, taskAnswer);
        } catch (ClassCastException ignored) {
        }
        throw new IllegalArgumentException("Invalid answer");
    }

    private static ExpressionTask.Generator getExpressionTaskGenerator(JSONArray jsonObject) {
        EnumSet<Operations> operations1 = EnumSet.noneOf(Operations.class);
        operations1.add(Operations.generateSum);
        ExpressionTask.Generator out = new ExpressionTask.Generator(-42, -42, operations1, 0);
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            double minNumber = ((Number) obj.get("min")).doubleValue();
            double maxNumber = ((Number) obj.get("max")).doubleValue();
            EnumSet<Operations> operations = EnumSet.noneOf(Operations.class);
            if (obj.get("plus").equals("yes")) {
                operations.add(Operations.generateSum);
            }
            if (obj.get("minus").equals("yes")) {
                operations.add(Operations.generateDifference);
            }
            if (obj.get("mul").equals("yes")) {
                operations.add(Operations.generateMultiplication);
            }
            if (obj.get("div").equals("yes")) {
                operations.add(Operations.generateDivision);
            }
            int accuracy = (int) (long) obj.get("accuracy");
            out.Add(minNumber, maxNumber, operations, accuracy);
        }
        return out;
    }

    private static Task getExpressionTask(JSONObject jsonObject) {
        double min = ((Number) jsonObject.get("min")).doubleValue();
        double max = ((Number) jsonObject.get("max")).doubleValue();
        EnumSet<Operations> operations = EnumSet.noneOf(Operations.class);
        if (jsonObject.get("plus").equals("yes")) {
            operations.add(Operations.generateSum);
        }
        if (jsonObject.get("minus").equals("yes")) {
            operations.add(Operations.generateDifference);
        }
        if (jsonObject.get("mul").equals("yes")) {
            operations.add(Operations.generateMultiplication);
        }
        if (jsonObject.get("div").equals("yes")) {
            operations.add(Operations.generateDivision);
        }
        int accuracy = (int) (long) jsonObject.get("accuracy");
        return new ExpressionTask(min, max, operations, accuracy);
    }

    private static EquationTask.Generator getEquationTaskGenerator(JSONArray jsonObject) {
        EnumSet<Operations> operations1 = EnumSet.noneOf(Operations.class);
        operations1.add(Operations.generateSum);
        EquationTask.Generator out = new EquationTask.Generator(-42, -42, operations1, 0);
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            double min = ((Number) obj.get("min")).doubleValue();
            double max = ((Number) obj.get("max")).doubleValue();
            EnumSet<Operations> operations = EnumSet.noneOf(Operations.class);
            if (obj.get("plus").equals("yes")) {
                operations.add(Operations.generateSum);
            }
            if (obj.get("minus").equals("yes")) {
                operations.add(Operations.generateDifference);
            }
            if (obj.get("mul").equals("yes")) {
                operations.add(Operations.generateMultiplication);
            }
            if (obj.get("div").equals("yes")) {
                operations.add(Operations.generateDivision);
            }
            int accuracy = (int) (long) obj.get("accuracy");
            out.Add(min, max, operations, accuracy);
        }
        return out;
    }

    private static Task getEquationTask(JSONObject jsonObject) {
        double min = ((Number) jsonObject.get("min")).doubleValue();
        double max = ((Number) jsonObject.get("max")).doubleValue();
        EnumSet<Operations> operations = EnumSet.noneOf(Operations.class);
        if (jsonObject.get("plus").equals("yes")) {
            operations.add(Operations.generateSum);
        }
        if (jsonObject.get("minus").equals("yes")) {
            operations.add(Operations.generateDifference);
        }
        if (jsonObject.get("mul").equals("yes")) {
            operations.add(Operations.generateMultiplication);
        }
        if (jsonObject.get("div").equals("yes")) {
            operations.add(Operations.generateDivision);
        }
        int accuracy = (int) (long) jsonObject.get("accuracy");
        return new EquationTask(min, max, operations, accuracy);
    }

    private static GroupTaskGenerator getGroupTaskGenerator(JSONArray jsonObject) throws IllegalClassFormatException {
        Collection<Task.Generator> result = new ArrayList<>();
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            String generator = (String) obj.get("type");
            Generators generators = switch (generator) {
                case "EquationTaskGenerator" -> Generators.EquationGenerator;
                case "ExpressionTaskGenerator" -> Generators.ExpressionGenerator;
                case "TextTaskGenerator" -> Generators.TextGenerator;
                default -> Generators.Error;
            };
            switch (generators) {
                case TextGenerator -> result.add(getTextTaskGenerator((JSONArray) obj.get("input")));
                case ExpressionGenerator -> result.add(getExpressionTaskGenerator((JSONArray) obj.get("input")));
                case EquationGenerator -> result.add(getEquationTaskGenerator((JSONArray) obj.get("input")));
                default -> throw new IllegalClassFormatException("Unknown type of generator");
            }
        }
        return new GroupTaskGenerator(result);
    }

    private static PoolTaskGenerator getPoolTaskGenerator(JSONArray jsonObject, boolean allowDuplicates) throws IllegalClassFormatException {
        Collection<Task> result = new ArrayList<>();
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            String generator = (String) obj.get("type");
            Tasks generators = switch (generator) {
                case "EquationTask" -> Tasks.EquationTask;
                case "ExpressionTask" -> Tasks.ExpressionTask;
                case "TextTask" -> Tasks.TextTask;
                default -> Tasks.Error;
            };
            switch (generators) {
                case TextTask -> result.add(getTextTask((JSONObject) obj.get("input")));
                case ExpressionTask -> result.add(getExpressionTask((JSONObject) obj.get("input")));
                case EquationTask -> result.add(getEquationTask((JSONObject) obj.get("input")));
                default -> throw new IllegalClassFormatException("Unknown type of generator");
            }
        }
        return new PoolTaskGenerator(allowDuplicates, result);
    }
}