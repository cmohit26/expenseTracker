package com.mohit.expense_backend.Security;

import com.mohit.expense_backend.Entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }
}