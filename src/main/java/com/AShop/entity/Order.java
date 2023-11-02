package com.AShop.entity;


import com.AShop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,//부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 cascade옵션 설정
                orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    //주문 상품 엔티티와 일대다 매핑을 함 외래키(order_id)가 order_item 테이블에 있으므로 연관관계의 주인은
    //OrderItem 엔티티임. Order엔티티가 주인이 아니므로 MappedBy 속성으로 연관 관계의 주인을 설정
    //속성값으로 order를 적은 이유는 OrderItem에 있는 ORder에 의해 관리 된다는 의미로 해석
    //즉 연관관계의 주인의 필드인 order의 mappedBy의 값으로 사용해서 세팅

    //하나의 주문이 여러개의 주문 상품을 갖으므로 List 자료형을 사용해서 매핑을 함

    /*private LocalDateTime regTime;

    private LocalDateTime updateTime;*/

    public  void addOrderItem(OrderItem orderItem){
        //orderItems에는 주문 상품 정보를 담아줌 orderItem 객체를 order 객체의 orderItems 에 추가
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        //order와 orderItem엔티티가 양ㅂ당향 참조이므로 orderItem에도 order 세팅
    }
    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        //주문한 회원 정보 세팅
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
            //상품 페이지에서는 1개의 상품을 주문하지만, 장바구니에서는 한번에 여러 개의 상품을 주문 할 수있따.,
            //따라서 여러개의 주문 상품을 담을 수 있도록 리스트형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체 추가
        }
        order.setOrderStatus(OrderStatus.ORDER);
        //주문 상태롤 ORDER 로 세팅
        order.setOrderDate(LocalDateTime.now());
        //현재 시간을 주문 시간으로 세팅
        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }



}
