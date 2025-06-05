package view;

import model.Student;
import model.StudentUpdateDto;
import repository.StudentRepository;
import utils.ValidationUtil;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import static utils.ValidationUtil.isValidGrade;

public class UI {
    private static final StudentRepository studentRepo = new StudentRepository();
    private static final Scanner scanner = new Scanner(System.in);
    public static void option() {
        System.out.println("-".repeat(20));
        System.out.println("Welcome to User Management System");
        System.out.println("-".repeat(20));
        System.out.println("""
                1. Add a new student
                2. View all students
                3. Update student details
                4. Delete a student
                5. Search students by grade
                6. List top-performing students
                7. Calculate the average score
                8. Bulk Insert Students
                9. Exit
                """);
        System.out.println("-".repeat(20));
    }
    public static void home() {
        while (true) {
            option();
            System.out.print("Insert your option: ");
            int opt = new Scanner(System.in).nextInt();
            try {
                switch (opt) {
                    case 1: {
                        System.out.println("Enter student details:");
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Age: ");
                        int age = scanner.nextInt(); scanner.nextLine();
                        if (age <= 0) {
                            System.out.println("❌ Age must be a positive number.");
                            break;
                        }
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        if (!ValidationUtil.isValidEmail(email)) {
                            System.out.println("❌ Invalid email format.");
                            break;
                        }
                        System.out.print("Grade: ");
                        String grade = scanner.nextLine().toUpperCase();
                        if (!isValidGrade(grade)) {
                            System.out.println("❌ Grade must be between A and F.");
                            break;
                        }
                        System.out.print("Score: ");
                        Double score = scanner.nextDouble();
                        if (score < 0 || score > 100) {
                            System.out.println("❌ Score must be between 0 and 100.");
                            break;
                        }
                        Student student = new Student(0, name, age, email, grade, score);
                        studentRepo.addStudent(student);
                        System.out.println("✅ Student added successfully!");
                        break;
                    }
                    case 2 : {
                        List<Student> students = studentRepo.getAllStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                        break;
                    }
                    case 3 : {
                        System.out.print("Enter student ID to update: ");
                        int id = UI.scanner.nextInt();
                        UI.scanner.nextLine();
                        System.out.print("New name: ");
                        String name = UI.scanner.nextLine();
                        System.out.print("New age: ");
                        int age = UI.scanner.nextInt();
                        if (!ValidationUtil.isValidAge(age)) throw new IllegalArgumentException("Age must be positive");
                        UI.scanner.nextLine();
                        System.out.print("New email: ");
                        String email = UI.scanner.nextLine();
                        if (!ValidationUtil.isValidEmail(email)) throw new IllegalArgumentException("Invalid email format");
                        System.out.print("New grade: ");
                        String grade = UI.scanner.nextLine().toUpperCase();
                        if (!isValidGrade(grade)) throw new IllegalArgumentException("Grade must be A-F");
                        System.out.print("New score: ");
                        double score = UI.scanner.nextDouble();
                        if (!ValidationUtil.isValidScore(score)) throw new IllegalArgumentException("Score must be 0-100");
                        UI.scanner.nextLine();
                        StudentUpdateDto dto = new StudentUpdateDto(name, age, email, grade, score);
                        UI.studentRepo.updateStudent(dto);
                        System.out.println("✅ Student updated successfully!");
                        break;
                    }
                    case 4 : {
                        System.out.print("Enter student ID to delete: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        studentRepo.deleteStudent(id);
                        System.out.println("✅ Student deleted successfully!");
                        break;
                    }
                    case 5 : {
                        System.out.print("Enter grade to search: ");
                        String grade = scanner.nextLine();
                        List<Student> students = studentRepo.searchStudentByGrade(grade);
                        if (students.isEmpty()) {
                            System.out.println("No students found with grade " + grade);
                        } else {
                            students.forEach(System.out::println);
                        }
                        break;
                    }
                    case 6 : {
                        List<Student> students = studentRepo.listTopPerformingStudents();
                        if (students.isEmpty()) {
                            System.out.println("No top-performing students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                        break;
                    }
                    case 7 : {
                        double avg = studentRepo.calculateAverageScore();
                        System.out.printf("📊 Average score of all students: %.2f%n", avg);
                        break;
                    }
                    case 8 : {
                        System.out.print("How many students do you want to insert? ");
                        int count = scanner.nextInt();
                        scanner.nextLine();
                        List<Student> students = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            System.out.printf("\nStudent #%d:%n", i + 1);
                            System.out.print("Name: ");
                            String name = scanner.nextLine();
                            System.out.print("Age: ");
                            int age = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Grade: ");
                            String grade = scanner.nextLine();
                            System.out.print("Score: ");
                            double score = scanner.nextDouble();
                            scanner.nextLine();
                            if (!ValidationUtil.isValidAge(age) ||
                                    !ValidationUtil.isValidEmail(email) ||
                                    !isValidGrade(grade) ||
                                    !ValidationUtil.isValidScore(score)) {
                                System.out.println("⚠️ Invalid input. Skipping this student.");
                                continue;
                            }
                            students.add(new Student(0, name, age, email, grade, score));
                        }
                        if (!students.isEmpty()) {
                            studentRepo.addStudentsBatch(students);
                            System.out.println("✅ Bulk insert completed for " + students.size() + " students.");
                        } else {
                            System.out.println("No valid students to insert.");
                        }
                        break;
                    }
                    case 9 : {
                        System.out.println("Thank you for visiting.");
                        System.exit(0);
                    }
                    default : System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception exception) {
                System.out.println("❌ Error: " + exception.getMessage());
            }
        }
    }
}
