package com.AShop.repository;


import com.AShop.dto.CartDetailDto;
import com.AShop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
    //카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회

    @Query("select  new  com.AShop.dto.CartDetailDto(ci.id , i.itemNm , i.price ,ci.count , im.imgUrl)" +
            "from CartItem ci , ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y'  " +
            "order by ci.regTime desc")
    //CartDetailDto의 생성자를 이용하여 DTO를 반환할때는 com.AShop.dto.CartDetailDto(ci.id , i.itemNm , i.price ,ci.count , im.imgUrl)
    //처럼 new 키와드와 해당 DTO의 패키지, 클래스 명을 적어주어야하고
    //ㅈ생성자의 파라미터 순서는 DTO클래스에 명시한 순으로 넣어주어야함.
    //장바구니에 담겨있는 상품의 대표 이미지만 가지고 오도록 조건문 작성.
    List<CartDetailDto> findCartDetailDtoList(Long cartId);


}
