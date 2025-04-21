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

	// お問い合わせ入力画面
	@GetMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("contactForm", new ContactForm());
		return "contact";
	}

	// 入力 → 確認画面へ
	@PostMapping("/contact")
	public String contact(@Validated @ModelAttribute("contactForm") ContactForm contactForm,
			BindingResult errorResult,
			HttpServletRequest request) {
		if (errorResult.hasErrors()) {
			return "contact";
		}

		HttpSession session = request.getSession();
		session.setAttribute("contactForm", contactForm);

		return "redirect:/contact/confirm";
	}

	// 確認画面
	@GetMapping("/contact/confirm")
	public String confirm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");
		model.addAttribute("contactForm", contactForm);
		return "confirmation";
	}

	// 確認 → 登録処理
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

	// 完了画面
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

	// 管理画面一覧
	@GetMapping("/admin/contacts")
	public String listContacts(Model model) {
		model.addAttribute("contacts", contactRepository.findAll());
		return "contact_list";
	}

	// 詳細画面（編集前の導線）
	@GetMapping("/admin/contacts/{id}")
	public String showContactDetail(@PathVariable Long id, Model model) {
		Optional<Contact> contactOpt = contactRepository.findById(id);
		if (contactOpt.isEmpty()) {
			return "redirect:/admin/contacts";
		}
		model.addAttribute("contact", contactOpt.get());
		return "contact_detail";
	}

	// 編集画面（詳細から遷移）
	@GetMapping("/admin/contacts/{id}/edit")
	public String editContact(@PathVariable Long id, Model model) {
		Optional<Contact> contactOpt = contactRepository.findById(id);
		if (contactOpt.isEmpty()) {
			return "redirect:/admin/contacts";
		}

		Contact contact = contactOpt.get();
		ContactForm form = new ContactForm();
		form.setLastName(contact.getLastName());
		form.setFirstName(contact.getFirstName());
		form.setEmail(contact.getEmail());
		form.setPhone(contact.getPhone());
		form.setZipCode(contact.getZipCode());
		form.setAddress(contact.getAddress());
		form.setBuildingName(contact.getBuildingName());
		form.setContactType(contact.getContactType());
		form.setBody(contact.getBody());

		model.addAttribute("contactForm", form);
		model.addAttribute("contactId", contact.getId());

		return "contact_edit";
	}

	// 編集更新処理
	@PostMapping("/admin/contacts/{id}/edit")
	public String updateContact(@PathVariable Long id,
			@Validated @ModelAttribute("contactForm") ContactForm form,
			BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("contactId", id);
			return "contact_edit";
		}

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

	// 削除処理
	@PostMapping("/admin/contacts/{id}/delete")
	public String deleteContact(@PathVariable Long id) {
		contactRepository.deleteById(id);
		return "redirect:/admin/contacts";
	}
}
