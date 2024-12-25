package com.example.SmartContactManageProject.Controller;


import com.example.SmartContactManageProject.Dao.UserRepository;
import com.example.SmartContactManageProject.Entity.User;
import com.example.SmartContactManageProject.Helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


//    @GetMapping("/test")
//    @ResponseBody
//    public String home(){
//        User user = new User();
//        user.setName("Pratik Pedhavi");
//        user.setEmail("pratikpd@gmail.com");
//        userRepository.save(user);
//        return "Working";
//    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "Home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "About";
    }

    @RequestMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("title", "Signup - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    //    this handler for register user
//    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               Model model, HttpSession session) throws Exception {

        // Check if the user has agreed to the terms
        if (!agreement) {
//            System.out.println("You have not agreed to the terms and conditions");
//            throw new Exception("You have not agreed to the terms and conditions");
            session.setAttribute("message", new Message("You have not agreed to the terms and conditions", "alert-danger"));
            return "signup"; // Return to the signup page with the error message
        }

        // Check if there are any validation errors
        if (result1.hasErrors()) {
            System.out.println("Error"+result1.toString());
            // If there are errors, add the user object to the model for form repopulation
            model.addAttribute("user", user);
            // Send validation error messages to the session
//            session.setAttribute("message", new Message("Please correct the errors in the form", "alert-danger"));
            return "signup"; // Return to the signup page with the validation errors
        }

        try {
            // Set default values for the user
            user.setRole("Role_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
//            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            System.out.println("Agreement"+agreement);
            System.out.println("User "+user);

            // Save the user
            User result = this.userRepository.save(user);

            // Clear the user object from the form for a clean state after successful registration
            model.addAttribute("user", new User());

            // Set success message in session
            session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));

            return "signup"; // Return to the signup page with success message
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user); // Repopulate the form with entered data
            session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
            return "signup"; // Return to the signup page with error message
        }
    }


}
