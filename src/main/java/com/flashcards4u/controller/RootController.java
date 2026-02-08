package com.flashcards4u.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RootController {

    // TODO: serve content from CardController and DeckController via index.html
    @RequestMapping("/")
    public ModelAndView getRoot() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }
}