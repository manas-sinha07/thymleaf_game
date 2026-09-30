package com.example.thymleaf_game;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GameController {

    @Autowired
    private GameRepository gameRepository;


    // =========================
    // HOME PAGE
    // =========================

    @GetMapping("/")
    public String home() {
        return "index";
    }


    // =========================
    // LOGIN / REGISTER PAGE
    // =========================

    @GetMapping("/login")
    public String showRegistrationForm(Model model) {

        model.addAttribute("game", new Game());

        return "registration";
    }


    // =========================
    // SAVE PLAYER
    // =========================

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute Game game,
            @RequestParam("imageFile") MultipartFile imageFile)
            throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {

            game.setImage(imageFile.getBytes());
        }

        gameRepository.save(game);

        return "redirect:/showGame";
    }


    // =========================
    // SHOW PLAYERS
    // =========================

    @GetMapping("/showGame")
    public String showAllUsers(Model model) {

        List<Game> games = gameRepository.findAll();

        for (Game game : games) {

            if (game.getImage() != null) {

                String base64Image =
                        Base64.getEncoder()
                                .encodeToString(game.getImage());

                game.setBase64Image(base64Image);
            }
        }

        model.addAttribute("games", games);

        return "game-list";
    }
}