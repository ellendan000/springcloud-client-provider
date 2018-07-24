package net.shadow.springcloud.controller;

import net.shadow.springcloud.exception.InvalidIdException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}/username")
    public String name(@PathVariable String id) {
        boolean flag = Optional.of(id)
                .orElse("")
                .length() == 3;

        if (flag) {
            return "user" + id;
        } else {
            throw new InvalidIdException();
        }
    }

}
