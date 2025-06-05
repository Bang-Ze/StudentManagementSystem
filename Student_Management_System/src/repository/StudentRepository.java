package repository;

import database.DatabaseConnectionConfig;
import model.Student;
import model.StudentUpdateDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public static List<Student> getAllStudent() {
        String sql = "SELECT * FROM student";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword
        );
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            List<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setEmail(resultSet.getString("email"));
                student.setGrade(resultSet.getString("grade"));
                student.setScore(resultSet.getDouble("score"));
                studentList.add(student);
            }
            return studentList;
        } catch (Exception exception) {
            System.out.println("Error getting all student data: " + exception.getMessage());
        }
        return null;
    }
    public int addStudent(Student student) {
        String sql = """
                INSERT INTO student(name, age, email, grade, score)
                VALUES (?,?,?,?,?,?);
                """;
        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword
        );
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getGrade());
            preparedStatement.setDouble(5, student.getScore());
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Data has been insert successfully.");
                return rowAffected;
            } else {
                System.out.println("Data is failed to insert");
            }
            return 0;
        } catch (Exception exception) {
            System.out.println("Error inserting new student data: " + exception.getMessage());
        }
        return 0;
    }
    public int updateStudent(StudentUpdateDto StudentUpdateDto) {
        String sql = """
                UPDATE student
                SET name = ?,
                age = ?,
                email = ?,
                grade = ?,
                score = ?,
                WHERE id = ?
                """;
        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword
        );
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, StudentUpdateDto.name());
            preparedStatement.setInt(2, StudentUpdateDto.age());
            preparedStatement.setString(3, StudentUpdateDto.email());
            preparedStatement.setString(4, StudentUpdateDto.grade());
            preparedStatement.setDouble(5, StudentUpdateDto.score());
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Data has been update successfully.");
                return rowAffected;
            } else {
                System.out.println("Data is failed to update");
            }
            return 0;
        } catch (Exception exception) {
            System.out.println("Error updating student's data: " + exception.getMessage());
        }
        return 0;
    }
    public void deleteStudent(int id) throws SQLException {
        String sql = """
            DELETE FROM student
            WHERE id = ?""";

        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    public List<Student> searchStudentByGrade(String grade) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student Where grade = ?";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword
        );
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, grade);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                students.add(new Student(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("age"),
                                resultSet.getString("email"),
                                resultSet.getString("grade"),
                                resultSet.getDouble("score")
                        )
                );
            }
        }
        return students;
    }
    public List<Student> listTopPerformingStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = """
            SELECT * FROM student
            WHERE score >= 90""";

        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getString("grade"),
                        resultSet.getDouble("score")
                ));
            }
        }
        return students;
    }
    public double calculateAverageScore() throws SQLException {
        String sql = """
            SELECT AVG(score) AS avg_score FROM student""";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next() ? resultSet.getDouble("avg_score") : 0.0;
        }
    }
    public void addStudentsBatch(List<Student> students) throws SQLException {
        String sql = """
            INSERT INTO student (name, age, email, grade, score)
            VALUES (?, ?, ?, ?, ?)""";
        try (Connection connection = DriverManager.getConnection(
                DatabaseConnectionConfig.databaseUrl,
                DatabaseConnectionConfig.databaseUserName,
                DatabaseConnectionConfig.databasePassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (Student student : students) {
                preparedStatement.setString(1, student.getName());
                preparedStatement.setInt(2, student.getAge());
                preparedStatement.setString(3, student.getEmail());
                preparedStatement.setString(4, student.getGrade());
                preparedStatement.setDouble(5, student.getScore());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        }
    }
}
