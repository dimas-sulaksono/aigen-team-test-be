package com.schoolpayment.team.security;

import com.schoolpayment.team.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        // Gunakan email jika tersedia, jika tidak gunakan NIS sebagai username
        return user.getEmail() != null ? user.getEmail() : user.getNis();
    }

    @Override
    public boolean isAccountNonExpired(){
        return user.getDeletedAt() == null; // Akun dianggap expired jika soft delete dilakukan
    }

    @Override
    public boolean isAccountNonLocked(){
        return user.getDeletedAt() == null; // Akun dianggap terkunci jika soft delete dilakukan
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return user.getDeletedAt() == null; // Akun hanya aktif jika tidak dihapus
    }
}
