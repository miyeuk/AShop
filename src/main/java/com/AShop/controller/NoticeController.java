package com.AShop.controller;


import com.AShop.entity.Notice;

import com.AShop.repository.NoticeRepository;
import com.AShop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;


    @GetMapping("/noticeList")
    public String noticeList(Model model,
                             @RequestParam(defaultValue = "0", name = "page") int page,
                             @RequestParam(defaultValue = "5", name = "size") int size) {
        // 현재 페이지 계산을 위한 로직
        int currentPage = Math.max(0, page);
        int maxPageRange = 5; // 표시할 페이지 목록의 최대 크기
        int nextPage = currentPage + 1;

        Pageable pageable = PageRequest.of(currentPage, size); // 페이지 번호와 페이지 크기 설정
        Page<Notice> notices = noticeRepository.findAll(pageable);
        int totalPages = notices.getTotalPages(); // 전체 페이지 수



        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = Math.max(0, currentPage - maxPageRange + 1); i <= Math.min(currentPage + maxPageRange, totalPages - 1); i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("noticeList", notices);
        // 페이지 정보를 모델에 추가
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", notices.getTotalPages());

        return "notice/noticelist";

    }

    @GetMapping("/noticeWrite")
    public String saveGet() {
        return "notice/noticeWrite";
    }

    @PostMapping("/noticeWrite")
    public String savePost(Notice notice) {
        noticeService.save(notice);
        return "redirect:/noticeList";
    }

    @GetMapping("/noticeDetail/{idx}")
    public String detail(@PathVariable int idx, Model model){

        model.addAttribute("notice" , noticeService.detail(idx));
        return "/noticeDetail";
    }


    @GetMapping("/noticeUpdate/{idx}")
    public String updateGet(@PathVariable int idx, Model model) {
        model.addAttribute("notice", noticeService.detail(idx));
        return "/noticeUpdate";
    }

    @PostMapping ("/noticeUpdate")
    public String updatePost( Notice notice ) {
        noticeService.save(notice);
        return "redirect:/noticeList";
    }

    @GetMapping("/delete/{idx}")
    public String delete(@PathVariable int idx) {
        noticeService.delete(idx);
        return "redirect:/noticeList";

    }


}