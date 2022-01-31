package com.senacor.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotNull
    @Getter
    @Setter
    private String username;

    @NotNull
    @Getter
    @Setter
    private String password;
}
