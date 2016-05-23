package com.example.service;

import com.example.domain.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // LoginUserDetailServiceをコンポーネント・スキャンの対象とする
public class LoginUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository; // Userオブジェクトを取得するためにUserRepositoryをインジェクション

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(username); // UserRepository#findOneでユーザ名からUserオブジェクトを取得
        if (user == null) {
            // Userオブジェクトが見つからない場合は、findOneメソッドはnullを返す
            throw new UsernameNotFoundException("The requested user is not found.");
        }
        return new LoginUserDetails(user); // Userオブジェクトが見つかった場合は、LoginUserDetailsにラップして返却
    }
}