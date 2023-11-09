package com.AShop.service;


import com.AShop.entity.Notice;
import com.AShop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    @Transactional
    public void save(Notice notice){
        noticeRepository.save(notice);

    }
    public List<Notice> list(){
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC,"idx"));
        /*idx기준으로 내림차순 정렬*/
    }


    public Notice detail(int idx){
        return noticeRepository.findById(idx).orElse(null);
        /**/
    }
    public void update(Notice notice){
        noticeRepository.findById(notice.getIdx());
        noticeRepository.save(notice);
    }





    public void delete(int idx){
        noticeRepository.deleteById(idx);
    }
}
