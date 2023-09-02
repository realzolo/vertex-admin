package com.onezol.vertex.core.security.authentication.runner;

import com.onezol.vertex.common.constant.enums.AccountStatus;
import com.onezol.vertex.common.constant.enums.Gender;
import com.onezol.vertex.core.security.authentication.service.UserAuthService;
import com.onezol.vertex.core.security.management.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
@Slf4j
public class AccountInitRunner implements ApplicationRunner {
    @Value("${app.config.username:admin}")
    private String username;
    @Value("${app.config.password:admin}")
    private String password;

    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;

    public AccountInitRunner(UserAuthService userAuthService, PasswordEncoder passwordEncoder) {
        this.userAuthService = userAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserEntity user = userAuthService.getUserByUsername(username);
        if (Objects.isNull(user)) {
            log.info("初始化系统用户[{}]", username);
            user = new UserEntity();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setNickname("系统管理员");
            user.setName("系统管理员");
            user.setIntroduction("~");
            user.setAvatar("");
            user.setGender(Gender.MALE.getCode());
            user.setBirthday(LocalDate.now());
            user.setPwdExpDate(LocalDate.now().plusMonths(1));
            user.setStatus(AccountStatus.ACTIVE);
            userAuthService.save(user);
        }
    }
}
