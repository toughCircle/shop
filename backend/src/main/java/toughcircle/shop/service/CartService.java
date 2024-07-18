package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Cart;
import toughcircle.shop.model.Entity.CartItem;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.dto.CartItemDto;
import toughcircle.shop.model.dto.request.AddCartItemRequest;
import toughcircle.shop.model.dto.request.UpdateQuantityRequest;
import toughcircle.shop.repository.CartItemRepository;
import toughcircle.shop.repository.CartRepository;
import toughcircle.shop.repository.ProductRepository;

import java.util.List;

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

    @Transactional
    public void saveCartItem(String token, Long cartId, AddCartItemRequest request) throws BadRequestException {
        tokenUserService.getUserByToken(token);

        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new BadRequestException("Cart not found with cartId: " + cartId));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new BadRequestException("Product not found with productId: " + request.getProductId()));

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

    @Transactional
    public void updateQuantity(String token, Long cartItemId, UpdateQuantityRequest request) throws BadRequestException {
        tokenUserService.getUserByToken(token);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new BadRequestException("CartItem not found with cartItemId: " + cartItemId));

        cartItem.setQuantity(request.getQuantity());
    }

    public void deleteCartItem(String token, Long cartItemId) throws BadRequestException {
        tokenUserService.getUserByToken(token);

        cartItemRepository.deleteById(cartItemId);
    }

    public List<CartItemDto> getCartItem(String token, Long cartId) throws BadRequestException {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new BadRequestException("Cart not found with cartId: " + cartId));

        List<CartItem> cartItemList = cart.getCartItemList();
        List<CartItemDto> list = cartItemList.stream().map(this::convertToDto).toList();
        return list;

    }

    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCartItemId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setProduct(productService.convertToDto(cartItem.getProduct(), false));

        return cartItemDto;
    }
}
