package com.AShop.service;


import com.AShop.dto.CartDetailDto;
import com.AShop.dto.CartItemDto;
import com.AShop.dto.CartOrderDto;
import com.AShop.dto.OrderDto;
import com.AShop.entity.Cart;
import com.AShop.entity.CartItem;
import com.AShop.entity.Item;
import com.AShop.entity.Member;
import com.AShop.repository.CartItemRepository;
import com.AShop.repository.CartRepository;
import com.AShop.repository.ItemRepository;
import com.AShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartSevice {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;


    public Long addCart(CartItemDto cartItemDto, String email){
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        //장바구니에 담을 상품 엔티티를 조회
        Member member = memberRepository.findByEmail(email);
        //현재 로그인 한 회원 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        //현재 로그인한 회원의 장바구니 엔티티 조회
        if(cart == null ) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
            //상품을 처음으로 장바구니에 담을 경우 해당회원의 장바구니 엔티티 생성
        }

        CartItem savedCartItem =cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        //현재 상품이 장바구니에 이미 들어가 있는지 조회

        if (savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        //장바구니에 이미 있던 상품일 경우 기존 수량에 현재 장바구니에 담을 수량 만큼 더함
        }else {
            CartItem cartItem=CartItem.createCartItem(cart , item , cartItemDto.getCount());
            //장바구니 엔티티, 상품 엔티티 ,장바구니에 담을 수량을 이용해서 CartItem 엔티티를 생성
            cartItemRepository.save(cartItem);
            return cartItem.getId();
            //장바구니에 들어갈 상품을 저장.
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member =memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            return cartDetailDtoList;
            //장바구니에 상품을 한번도 안 담았을 경우 장바구니 엔티티가 없으므로 빈 리스트 반환
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        //장바구니에 담겨있는 상품 정보 조회
        return cartDetailDtoList;

    }


    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member curMember = memberRepository.findByEmail(email);
        //현재 로그인한 회원조회 CURRENT MEMBER
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember();
        //장바구니 상품을 저장한 회원 조회
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;

        }
        return true;
        //현재 로그인한 회원과 장바구니 상품을 저장한 회원이 다를 경우 false,같으면 true반환
        }

    public  void updateCartItemCount (Long cartItemId , int count){
        CartItem cartItem =cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
        //장바구니 수량 업데이트
    }

    public void deleteCartItem(Long cartItemId){
        CartItem cartItem =cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList , String email){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);

        }

        Long orderId = orderService.orders(orderDtoList, email);


            for (CartOrderDto cartOrderDto : cartOrderDtoList){
                CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                        .orElseThrow(EntityNotFoundException :: new);
                cartItemRepository.delete(cartItem);

        }

            return orderId;
    }
}
