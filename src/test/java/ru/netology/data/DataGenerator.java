package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@Data
public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    static void setUpAll(RegistrationDto registrationDto) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(registrationDto) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static RegistrationDto validActiveUser() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().username().toLowerCase();
        String password = faker.internet().password();
        String status = "active";
        RegistrationDto registrationDto = new RegistrationDto(login, password, status);
        setUpAll(registrationDto);
        return registrationDto;
    }

    public static RegistrationDto validBlockedUser() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().username().toLowerCase();
        String password = faker.internet().password();
        String status = "blocked";
        RegistrationDto registrationDto = new RegistrationDto(login, password, status);
        setUpAll(registrationDto);
        return registrationDto;
    }

    public static RegistrationDto inCorrectLogin() {
        Faker faker = new Faker(new Locale("en"));
        String login = "vasya";
        String password = faker.internet().password();
        String status = "active";
        setUpAll(new RegistrationDto(login, password, status));
        return new RegistrationDto("dima", password, status);
    }

    public static RegistrationDto inCorrectPassword() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().username().toLowerCase();
        String password = "password";
        String status = "active";
        setUpAll(new RegistrationDto(login, password, status));
        return new RegistrationDto(login, "wrongPassword", status);
    }
}
