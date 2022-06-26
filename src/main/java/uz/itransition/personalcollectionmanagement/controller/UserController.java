package uz.itransition.personalcollectionmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.itransition.personalcollectionmanagement.projection.ProfileProjection;
import uz.itransition.personalcollectionmanagement.projection.UserProjection;
import uz.itransition.personalcollectionmanagement.service.UserService;

import java.util.List;
import java.util.UUID;

import static uz.itransition.personalcollectionmanagement.utils.Constants.DEFAULT_PAGE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/my-collections")
    public String getProfilePage(Model model) {
        ProfileProjection userProfile = userService.getUserProfile();
        model.addAttribute("userProfile", userProfile);
        return "profile";
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/management")
    public String getAllUsers(Model model,
                              @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer page,
                              @RequestParam(value = "sortBy",defaultValue = "email") String sortBy,
                              @RequestParam(value = "sortDir",defaultValue = "asc") String sortDir) {
        Page<UserProjection> users = userService.getAllUsers(page,sortBy,sortDir);
        model.addAttribute("users", users);
        model.addAttribute("sortDirection", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        return "user-management";
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/change-role/{userId}")
    public String changeUserRole(@PathVariable UUID userId) {
        userService.changeUserRole(userId);
        return "redirect:/user/management";
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/change-status/{userId}")
    public String changeUserStatus(@PathVariable UUID userId) {
        userService.changeUserStatus(userId);
        return "redirect:/user/management";
    }
}
