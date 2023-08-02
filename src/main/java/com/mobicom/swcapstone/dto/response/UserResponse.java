package com.mobicom.swcapstone.dto.response;

import com.mobicom.swcapstone.domain.Role;
import com.mobicom.swcapstone.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private long id;

    private String userId;
    private String password;
    private Role role;



    public UserResponse(User user){
        this.id = user.getId();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
