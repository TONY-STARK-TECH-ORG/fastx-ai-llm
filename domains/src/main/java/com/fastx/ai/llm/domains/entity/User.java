package com.fastx.ai.llm.domains.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fastx.ai.llm.domains.base.BaseDO;
import com.fastx.ai.llm.domains.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author stark
 * @since 2024-12-07
 */
@Getter
@Setter
@TableName("t_user")
public class User extends BaseDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String email;

    private String password;

    private String role;

    private String status;

    private String username;

    private LocalDateTime lastLogin;

    private String profileImageUrl;

    private String bio;

    private String expertiseAreas;

    private String preferredLanguage;

    private String socialLinks;

    private String authProvider;

    private String authOpenId;

    public static User of(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    public UserDTO to() {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(this, userDTO);
        return userDTO;
    }
}
