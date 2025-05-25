package org.example;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UserDao userDao = new UserDao();
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
                    }
                    default -> System.out.println("Неверный ввод, попробуйте снова.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
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
        userDao.createUser(user);
    }

    private static void readUser() {
        System.out.print("Введите ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userDao.getUser(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private static void readAllUsers() {
        List<User> users = userDao.getAllUsers();
        if (users != null && !users.isEmpty()) {
            users.forEach(System.out::println);
        } else {
            System.out.println("Пользователи не найдены.");
        }
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());

        User user = userDao.getUser(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
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

        userDao.updateUser(user);
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());

        userDao.deleteUser(id);
    }
}
