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

        if (authentication == null) {
            throw new RuntimeException("No authentication found");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new RuntimeException("User is not authenticated");
        }

        return userPrincipal.getUser();
    }

    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }
}