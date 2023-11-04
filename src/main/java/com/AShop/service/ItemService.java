package com.AShop.service;


import com.AShop.dto.ItemFormDto;
import com.AShop.dto.ItemImgDto;
import com.AShop.dto.ItemSearchDto;
import com.AShop.dto.MainItemDto;
import com.AShop.entity.Item;
import com.AShop.entity.ItemImg;
import com.AShop.repository.ItemImgRepository;
import com.AShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();
        //상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체 생성
        itemRepository.save(item);
        //상품 데이터 저장


        //이미지 등록
        for( int i=0 ; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)
                itemImg.setRepimgYn("Y");
            //첫 번째 이미지 일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅 나머지 상품 이미지는 "N"로 설정
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg , itemImgFileList.get(i) );
            //상품 이미지 정보를 저장
        }
                return item.getId();

    }



    public Long updateItem(ItemFormDto itemFormDto , List<MultipartFile> itemImgFileList) throws  Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        //상품 등록 화면으로 부터 전달 받은 상품 아이디를 이요아여 상품 엔티티를 조회
        item.updateItem(itemFormDto);
        //상품 등록 화면으로 부터 전달 받은 ItemFormDto 를 통해 상품 엔티티를 업데이트 함
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
    //ㅅ상품 이미지 아이디 리스트를 조회

        //이미지 등록
        for (int i=0; i< itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        //상품 이미지를 업데이트 하기 위해 updateItemImg() 메소드에 상품 이미지 아이디와 , 상품 이미지 파일 정보를 파라미터로 전달
        }
        return item.getId();
    }


    @Transactional( readOnly = true)
    //상품 데이터를 읽어오는 트랙잭션을 읽기 전용으로 설정
    //이럴경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능 향상
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //해당 상품의 이미지를 조회 . 등록 순으로 가지고 오기 위해서 상품 이미지 아이디 오름 차순으로 가지고옴
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList){
            //조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item= itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        // 상품의 아이디를 통해 상품 엔티티 조회. 존재 하지 않을 때는 EntityNotFoundException 발생
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    @Transactional( readOnly = true)
    public Page<Item> getAdminPage(ItemSearchDto itemSearchDto , Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional( readOnly = true )
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto , pageable);
    }
}
