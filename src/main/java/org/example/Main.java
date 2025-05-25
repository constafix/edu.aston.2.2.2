package org.example;

import org.example.dao.UserDao;
import org.example.entity.User;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Получить пользователя по ID");
            System.out.println("3. Обновить пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("5. Показать всех пользователей");
            System.out.println("0. Выход");
            System.out.print("> ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите email: ");
                    String email = scanner.nextLine();
                    System.out.print("Введите возраст: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    userDao.createUser(new User(name, email, age));
                }
                case 2 -> {
                    System.out.print("Введите ID пользователя: ");
                    Long id = scanner.nextLong();
                    scanner.nextLine();
                    User user = userDao.getUser(id);
                    System.out.println(user);
                }
                case 3 -> {
                    System.out.print("Введите ID пользователя для обновления: ");
                    Long id = scanner.nextLong();
                    scanner.nextLine();
                    User user = userDao.getUser(id);
                    if (user != null) {
                        System.out.print("Введите новое имя: ");
                        user.setName(scanner.nextLine());
                        System.out.print("Введите новый email: ");
                        user.setEmail(scanner.nextLine());
                        System.out.print("Введите новый возраст: ");
                        user.setAge(scanner.nextInt());
                        scanner.nextLine();
                        userDao.updateUser(user);
                    }
                }
                case 4 -> {
                    System.out.print("Введите ID пользователя для удаления: ");
                    Long id = scanner.nextLong();
                    scanner.nextLine();
                    userDao.deleteUser(id);
                }
                case 5 -> {
                    List<User> users = userDao.getAllUsers();
                    users.forEach(System.out::println);
                }
                case 0 -> running = false;
                default -> System.out.println("Неверный выбор.");
            }
        }
        scanner.close();
    }
}
