package com.devSoft.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.devSoft.Model.User;
import com.devSoft.Repository.BlockRepository;
import com.devSoft.Repository.CertificateRepository;
import com.devSoft.Repository.DepartmentRepository;
import com.devSoft.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ADD THESE
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // ================= LOGIN PAGE =================

    @GetMapping("/")
    public String getLogin() {

        return "LoginForm";
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public String postLogin(@ModelAttribute User user,
                            Model model,
                            HttpSession session) {

        User usr = userService.login(
                user.getEmail(),
                user.getPassword()
        );

        if (usr != null) {

            session.setAttribute("activeuser", usr);

            session.setMaxInactiveInterval(500);

            // ================= ROLE LOGIC =================

            if (usr.getRole().equalsIgnoreCase("student")) {

                return "redirect:/studentHome";

            } else if (usr.getRole().equalsIgnoreCase("HR")) {

                return "redirect:/hrHome";

            } else {

                return "redirect:/home";
            }
        }

        model.addAttribute("message", "User does not exist");

        return "LoginForm";
    }

    // ================= SIGNUP PAGE =================

    @GetMapping("/signup")
    public String getSignup() {

        return "SignupForm";
    }

    // ================= SIGNUP =================

    @PostMapping("/signup")
    public String postSignup(@ModelAttribute User u, Model model) {

        if (userService.emailExists(u.getEmail())) {
            model.addAttribute("message", "Email already registered!");
            return "SignupForm";
        }

        userService.signup(u);

        return "LoginForm";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "LoginForm";
    }

    // ================= PROFILE =================

    @GetMapping("/profile")
    public String getProfile() {

        return "ProfileForm";
    }

    // ================= STUDENT DASHBOARD =================

    @GetMapping("/studentHome")
    public String studentHome(Model model, HttpSession session) {

        User user = (User) session.getAttribute("activeuser");
        if (user == null) return "LoginForm";

        model.addAttribute("user", user);
        model.addAttribute("totalCertificates", certificateRepository.count());
        model.addAttribute("totalBlocks", blockRepository.count());

        return "StudentHome";
    }

    // ================= HR DASHBOARD =================

    @GetMapping("/hrHome")
    public String hrHome(Model model, HttpSession session) {

        User user = (User) session.getAttribute("activeuser");
        if (user == null) return "LoginForm";

        model.addAttribute("user", user);
        model.addAttribute("totalCertificates", certificateRepository.count());
        model.addAttribute("totalBlocks", blockRepository.count());
        model.addAttribute("totalDepartments", departmentRepository.count());

        return "HR-Home";
    }

    // ================= ADMIN DASHBOARD =================

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

        User user = (User) session.getAttribute("activeuser");
        if (user == null) return "LoginForm";

        model.addAttribute("user", user);
        model.addAttribute("totalCertificates", certificateRepository.count());
        model.addAttribute("totalBlocks", blockRepository.count());
        model.addAttribute("totalDepartments", departmentRepository.count());

        return "Home";
    }

}