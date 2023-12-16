package com.onezol.vertex.security.biz.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.common.constant.enums.AccountStatus;
import com.onezol.vertex.common.constant.enums.Gender;
import com.onezol.vertex.security.api.model.entity.UserEntity;
import com.onezol.vertex.security.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class InitAccountRunner implements ApplicationRunner {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.security.username:admin}")
    private String username;
    @Value("${app.security.password:admin}")
    private String password;

    public InitAccountRunner(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        long count = userService.count(
                Wrappers.<UserEntity>lambdaQuery()
                        .eq(UserEntity::getUsername, username)
        );
        if (count == 0) {
            String DEFAULT_SYSTEM_ADMINISTRATOR_NAME = "系统管理员";
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setNickname(DEFAULT_SYSTEM_ADMINISTRATOR_NAME);
            user.setName(DEFAULT_SYSTEM_ADMINISTRATOR_NAME);
            user.setIntroduction("～");
            user.setAvatar("");
            user.setGender(Gender.MALE);
            user.setBirthday(LocalDate.now());
            user.setPhone("");
            user.setEmail("");
            user.setPwdExpDate(LocalDate.now().plusMonths(1));
            user.setStatus(AccountStatus.ACTIVE);
            userService.save(user);
            log.info("用户[{}]初始化完成, 用户名: {}, 密码: {}", DEFAULT_SYSTEM_ADMINISTRATOR_NAME, username, password);
        }
    }
}
