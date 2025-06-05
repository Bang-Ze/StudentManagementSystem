package model;

public record StudentUpdateDto (
        String name,
        Integer age,
        String email,
        String grade,
        Double score
) {
}
