package org.example;

import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            boolean running = true;

            while (running) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Создать пользователя");
                System.out.println("2. Просмотреть пользователя по ID");
                System.out.println("3. Просмотреть всех пользователей");
                System.out.println("4. Обновить пользователя");
                System.out.println("5. Удалить пользователя");
                System.out.println("6. Выход");
                System.out.print("Введите номер: ");

                String input = scanner.nextLine();

                switch (input) {
                    case "1" -> createUser();
                    case "2" -> readUser();
                    case "3" -> readAllUsers();
                    case "4" -> updateUser();
                    case "5" -> deleteUser();
                    case "6" -> {
                        running = false;
                        System.out.println("Завершение работы...");
                        logger.info("Программа завершена пользователем.");
                    }
                    default -> System.out.println("Неверный ввод, попробуйте снова.");
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка в работе программы", e);
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
            logger.info("Ресурсы освобождены, приложение завершено.");
        }
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userService.createUser(user);
        logger.info("Создан пользователь: {}", user);
    }

    private static void readUser() {
        System.out.print("Введите ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userService.getUserById(id);
        if (user != null) {
            System.out.println(user);
            logger.info("Запрошен пользователь с ID {}", id);
        } else {
            System.out.println("Пользователь не найден.");
            logger.warn("Пользователь с ID {} не найден", id);
        }
    }

    private static void readAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users != null && !users.isEmpty()) {
            users.forEach(System.out::println);
            logger.info("Выведен список всех пользователей, количество: {}", users.size());
        } else {
            System.out.println("Пользователи не найдены.");
            logger.warn("Список пользователей пуст");
        }
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userService.getUserById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            logger.warn("Попытка обновления несуществующего пользователя с ID {}", id);
            return;
        }

        System.out.print("Введите новое имя (текущие: " + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) user.setName(name);

        System.out.print("Введите новый email (текущий: " + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isBlank()) user.setEmail(email);

        System.out.print("Введите новый возраст (текущий: " + user.getAge() + "): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isBlank()) {
            int age = Integer.parseInt(ageStr);
            user.setAge(age);
        }

        userService.updateUser(user);
        logger.info("Обновлен пользователь: {}", user);
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());

        userService.deleteUser(id);
        logger.info("Удален пользователь с ID {}", id);
    }
}
