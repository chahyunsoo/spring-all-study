package spring_mvc.springmvc.requestmapping;

import org.springframework.web.bind.annotation.*;

/**
 * [회원 관리 API]
 * 회원 목록 조회: GET /users
 * 회원 등록: POST /users
 * 회원 조회: GET /users
 * 회원 수정: PATCH /users/{userId}
 * 회원 삭제: DELETE /users/{userId}
 */
@RestController
@RequestMapping(value = "/mapping")
public class MappingClassController {

    @GetMapping("/users")
    public String getUsers() {
        return "get users";
    }

    @PostMapping("/users")
    public String addUser() {
        return "add user";
    }

    @GetMapping("/users/{userId}")
    public String findUser(@PathVariable("userId") String userId) {
        return "get one user: " + userId;
    }

    @PatchMapping("/users/{userId}")
    public String updateUser(@PathVariable("userId") String userId) {
        return "update user: " + userId;
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") String userId) {
        return "delete user: " + userId;
    }

}
