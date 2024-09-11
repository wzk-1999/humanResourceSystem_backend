package com.blueOcean.humanResourceSystem.Service;

import com.blueOcean.humanResourceSystem.Model.userCredentials;
import com.blueOcean.humanResourceSystem.Repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private CredentialsRepository credentialsRepository;

    public boolean login(String username, String password) {
        // 使用用户名查找用户
        Optional<userCredentials> userOptional = credentialsRepository.findUserByusername(username);

        if (userOptional.isPresent()) {
        userCredentials user = userOptional.get();

        // 验证密码
        if (password.equals(user.getPassword())) {
            // 密码匹配，登录成功
            return true;
        }
    }

    // 用户不存在或密码不匹配
        return false;
}



private String[] splitRole(userCredentials users) {
        if (users.getRole() == null) {
            return new String[]{"Staff"};
        }
        return users.getRole().split(",");
    }


}
