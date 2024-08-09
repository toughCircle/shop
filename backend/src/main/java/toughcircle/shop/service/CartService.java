package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.exception.exceptions.NotFoundException;
import toughcircle.shop.exception.exceptions.UnauthorizedException;
import toughcircle.shop.model.Entity.Cart;
import toughcircle.shop.model.Entity.CartItem;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.CartItemDto;
import toughcircle.shop.model.dto.request.AddCartItemRequest;
import toughcircle.shop.model.dto.request.UpdateQuantityRequest;
import toughcircle.shop.repository.CartItemRepository;
import toughcircle.shop.repository.CartRepository;
import toughcircle.shop.repository.ProductRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final TokenUserService tokenUserService;

    /**
     * 장바구니에 상품 추가
     * @param cartId 장바구니 ID
     * @param request 장바구니 상품 추가 요청 정보
     */
    @Transactional
    public void createCartItem(Long cartId, AddCartItemRequest request) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Cart not found with cartId: " + cartId));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found with productId: " + request.getProductId()));

        CartItem item = cartItemRepository.findByCart_idAndProduct_id(cartId, request.getProductId());
        if (item != null) {
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

            cartItemRepository.save(cartItem);
        }

    }

    /**
     * 장바구니 아이템의 수량 업데이트
     * @param token 사용자 토큰
     * @param cartItemId 장바구니 아이템 ID
     * @param request 수량 업데이트 요청 정보
     */
    @Transactional
    public void updateQuantity(String token, Long cartItemId, UpdateQuantityRequest request) {
        User user = tokenUserService.getUserByToken(token);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new NotFoundException("CartItem not found with cartItemId: " + cartItemId));

        if (Objects.equals(cartItem.getCart().getId(), user.getCart().getId())) {
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            throw new UnauthorizedException("User is not authorized to update this cart item.");
        }
    }

    /**
     * 장바구니 아이템 삭제
     * @param token 사용자 토큰
     * @param cartItemId 장바구니 아이템 ID
     */
    public void deleteCartItem(String token, Long cartItemId) {
        User user = tokenUserService.getUserByToken(token);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new NotFoundException("CartItem not found with cartItemId: " + cartItemId));

        if (Objects.equals(cartItem.getCart().getId(), user.getCart().getId())) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UnauthorizedException("User is not authorized to delete this cart item.");
        }

    }

    /**
     * 장바구니의 상품 목록 조회
     * @param token 사용자 토큰
     * @param cartId 장바구니 ID
     * @return 장바구니 아이템 DTO 리스트
     */
    public List<CartItemDto> getCartItems(String token, Long cartId) {
        User user = tokenUserService.getUserByToken(token);

        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Cart not found with cartId: " + cartId));

        if (Objects.equals(cart.getId(), user.getCart().getId())) {
            List<CartItem> cartItems = cart.getCartItemList();
            return cartItems.stream().map(this::convertToDto).toList();
        } else {
            throw new UnauthorizedException("User is not authorized to view this cart.");
        }
    }

    /**
     * 장바구니 아이템 DTO 변환
     * @param cartItem 장바구니 아이템 엔티티
     * @return 장바구니 아이템 DTO
     */
    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCartItemId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setProduct(productService.convertToDto(cartItem.getProduct(), false));

        return cartItemDto;
    }
}
