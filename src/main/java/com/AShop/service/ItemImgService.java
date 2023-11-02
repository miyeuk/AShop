package com.AShop.service;


import com.AShop.entity.ItemImg;
import com.AShop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {


    @Value("${itemImgLocation}")
    private String itemImgLocation;
    //@Value 어노테이션을 통해 properties파일에 등록한 itemLocation값을 불러와서 itemImgLocation 변수에 넣음
    //임폴트는 롬복이 아니라 빈즈 펙토리 어노테이션으로

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;


    public void  saveItemImg(ItemImg itemImg , MultipartFile itemImgFile)throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName= "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation , oriImgName , itemImgFile.getBytes());
            //사용자가 상품의 이미지를 등록 했다면 저장할 경로와 파일의 이름,파일을
            // 파일의 바이트 배열 파일 업로드 파라미터로 uploadFile 메소드를 호출
            //호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장
            imgUrl  = "/images/item/" + imgName;
            //저장한 상품 이미지를 불러올 경로를 설정 외부 리소스를 불러오는 urlPatterns로 WebMvcConfig 클래스에서
            //"/images/**"를 설정해 줬었음. 또한 properties에서 설정한 uploadPath 프로퍼티 경로아래 item폴더에
            // 이미지를 저장 하므로 상품이미지를 불러오는 경로로 "/images/item"을 붙여줌

        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName , imgUrl);
        //imgName :실제 로컬에 저장된 상품이미지 파일의 이름
        //oriImgName: 업로드 했던 상품 이미지 파일의 원래 이름
        //imgUrl :  업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImgRepository.save(itemImg);

    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile)throws Exception{
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //상품이미지를 수정한 경우 상품 이미지를 업데이트
            //상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티 조회

            //기존이미지 파일이 있을 경우 해당파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation , oriImgName , itemImgFile.getBytes());
            //업데이트한 상품 이미지 파일을 업로드
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //변경된 상품 이미지 정보를 세팅
            //중요한 점은 상품등록 때처럼 itemImgRepository.save()로직을 호출하지 않음
            //savedItemImg 엔티티는 현재 영속 상태이므로 데이터 변경 하는 것만으로 변경 감지기능이 동작해
            //트랜잭션이 끝날 때 update 쿼리가 실행됨 여기서 중요한 것은 엔티티가 영속 상태여야한다.
        }
    }
}
