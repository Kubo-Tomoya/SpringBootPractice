package com.example.demo.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactForm;
import com.example.demo.repository.ContactRepository;

@Controller
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact";
    }

    @PostMapping("/contact")
    public String contact(@Validated @ModelAttribute("contactForm") ContactForm contactForm, BindingResult errorResult, HttpServletRequest request) {
        if (errorResult.hasErrors()) {
            return "contact";
        }

        HttpSession session = request.getSession();
        session.setAttribute("contactForm", contactForm);

        return "redirect:/contact/confirm";
    }

    @GetMapping("/contact/confirm")
    public String confirm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");
        model.addAttribute("contactForm", contactForm);
        return "confirmation";
    }

    @PostMapping("/contact/register")
    public String register(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");

        Contact contact = new Contact();
        contact.setLastName(contactForm.getLastName());
        contact.setFirstName(contactForm.getFirstName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhone());
        contact.setZipCode(contactForm.getZipCode());
        contact.setAddress(contactForm.getAddress());
        contact.setBuildingName(contactForm.getBuildingName());
        contact.setContactType(contactForm.getContactType());
        contact.setBody(contactForm.getBody());

        contactRepository.save(contact);

        return "redirect:/contact/complete";
    }

    @GetMapping("/contact/complete")
    public String complete(Model model, HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return "redirect:/contact";
        }

        HttpSession session = request.getSession();
        ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");
        model.addAttribute("contactForm", contactForm);

        session.invalidate();

        return "completion";
    }

    
    @GetMapping("/admin/contacts")
    public String listContacts(Model model) {
        model.addAttribute("contacts", contactRepository.findAll());
        return "contact_list";
    }
    
    @GetMapping("/admin/contacts/{id}")
    public String showContactDetail(@PathVariable Long id, Model model) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return "redirect:/admin/contacts"; // なければ一覧に戻る
        }
        model.addAttribute("contact", contactOpt.get());
        return "contact_detail";
    }
    
    @GetMapping("/admin/contacts/{id}/edit")
    public String editContact(@PathVariable Long id, Model model) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return "redirect:/admin/contacts";
        }
        model.addAttribute("contact", contactOpt.get());
        return "contact_edit";
    }
    
    @PostMapping("/admin/contacts/{id}/edit")
    public String updateContact(@PathVariable Long id, @ModelAttribute Contact form) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isPresent()) {
            Contact contact = contactOpt.get();
            contact.setLastName(form.getLastName());
            contact.setFirstName(form.getFirstName());
            contact.setEmail(form.getEmail());
            contact.setPhone(form.getPhone());
            contact.setZipCode(form.getZipCode());
            contact.setAddress(form.getAddress());
            contact.setBuildingName(form.getBuildingName());
            contact.setContactType(form.getContactType());
            contact.setBody(form.getBody());
            contactRepository.save(contact);
        }
        return "redirect:/admin/contacts";
    }
    
    //削除機能
    @PostMapping("/admin/contacts/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
    
   





}
