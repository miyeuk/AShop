package com.AShop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter @Setter
@Table(name = "cart_item_id")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    //하나의 장바구니에는 여러개의 상품을 담을 수 있으므로 다대일 매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    // 상품정보를 알아야하므로 상품엔티티 매핑 함
    // 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있으므로 다대일 매핑

    private int count;//같은 상품을 장바구니에 몇개를 담을지 저장

    public static CartItem createCartItem(Cart cart ,Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }


    public void addCount(int count){
        this.count += count;
    // 장바구니에 기존 담겨있는 상품인데, 해당 상품을 추가로 장바구니에 담을 떄 기존 수량에 현재 담을 수량을 더해줄 때 사용
    }


    public void  updateCount(int count){
        this.count = count;
    }
}
