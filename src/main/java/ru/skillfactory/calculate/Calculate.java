package ru.skillfactory.calculate;

import java.util.Scanner;

public class Calculate {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Привет! Можешь воспользоваться моим калькулятором.");

        int firstOperand = readInt("Для начала я прошу тебя ввести первое любое ЧИСЛО:");
        char operation = readOperation();
        int secondOperand = readInt("Введите второе любое ЧИСЛО:");

        if(secondOperand == 0 && operation == '/') {
            System.out.println("Ошибка: деление на ноль невозможно.");
            return;
        }

        double result = calculate(firstOperand, secondOperand, operation);

        System.out.println("Результат: " + result);
    }

    private int readInt(String message) {
        System.out.println(message);

        while (!scanner.hasNextInt()) {
            System.out.println("К сожалению вы ошиблись... пожалуйста попробуй ввести число еще раз!");
            scanner.next();
        }

        int number = scanner.nextInt();
        System.out.println("Ты ввел число: " + number);

        return number;
    }

    private char readOperation() {
        System.out.println("Пожалуйста теперь введи ОДИН любой символ операции: +, -, *, /");

        while (true) {
            String input = scanner.next();

            if (input.length() == 1 && isSupportedOperation(input.charAt(0))) {
                char operation = input.charAt(0);
                System.out.println("Спасибо! Вы ввели операцию: " + operation);

                return operation;
            }

            System.out.println("К сожалению вы ошиблись...\n"
                    + "Пожалуйста введите ОДНУ из разрешенных операций: +, -, *, /");
        }
    }

    private double calculate(int firstOperand, int secondOperand, char operation) {
        return switch (operation) {
            case '+' -> firstOperand + secondOperand;
            case '-' -> firstOperand - secondOperand;
            case '*' -> firstOperand * secondOperand;
            case '/' -> (double) firstOperand / secondOperand;
            default -> throw new IllegalArgumentException("Неподдерживаемая операция: " + operation);
        };
    }

    private boolean isSupportedOperation(char operation) {
        return operation == '+' || operation == '-' || operation == '*' || operation == '/';
    }
}
