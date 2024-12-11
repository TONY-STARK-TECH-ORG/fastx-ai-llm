package com.fastx.ai.llm.web.controller.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author stark
 */
@Data
public class AuthRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    private String authProvider;
    private String authOpenId;

}
