package com.ecommers.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ecommers.exceptions.CartEmailRequiredException;
import com.ecommers.exceptions.CartFullNameRequiredException;
import com.ecommers.exceptions.CartNotPresentException;
import com.ecommers.exceptions.CartProcessingNotAllowDeletionException;
import com.ecommers.exceptions.CartStatusNotAllowCheckoutException;
import com.ecommers.exceptions.ProductNotPresentException;
import com.ecommers.exceptions.ProductQuantityInvalidException;
import com.ecommers.exceptions.ProductQuantityRequiredException;
import com.ecommers.exceptions.ProductStockInsufficientException;
import com.ecommers.models.Cart;
import com.ecommers.models.Product;
import com.ecommers.models.ProductInCart;
import com.ecommers.models.dto.CartDto;
import com.ecommers.models.dto.CartProductDto;
import com.ecommers.models.dto.ProductInCartDto;
import com.ecommers.repository.ICartRepository;
import com.ecommers.repository.IProductInCartRepository;
import com.ecommers.repository.IProductRepository;
import com.ecommers.service.Interface.ICartService;

@Service
public class CartService implements ICartService {
	private final ICartRepository cartRepository;
	private final IProductRepository productRepository;
	private final IProductInCartRepository productInCartRepository;
	
	public CartService( ICartRepository cartRepository, IProductRepository productRepository, IProductInCartRepository productInCartRepository) {
		super();
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.productInCartRepository = productInCartRepository;
	}

	@Override
	public CartDto create(CartDto cart_dto) {
		if (cart_dto.getFullName() == null){
			throw new CartFullNameRequiredException();
		}
		if (cart_dto.getEmail() == null) {
			throw new CartEmailRequiredException();
		}
		
		Cart cart = new Cart();
		BeanUtils.copyProperties(cart_dto, cart);
		this.cartRepository.save(cart);
		cart_dto.setId(cart.getId());
		return cart_dto;
	}

	@Override
	public CartDto get(long id) {
		CartDto cardDto = new CartDto();
		Optional<Cart> cart  = this.cartRepository.findById(id);
		if (!cart.isPresent()) {
			throw new CartNotPresentException();
		} 
		
		BeanUtils.copyProperties(cart.get(), cardDto);
		return cardDto;
	}
	
	 @Override
	 public CartDto addProducts(long id, CartProductDto cartProduct) {
		Optional<Cart> cart  = this.cartRepository.findById(id);
		if (!cart.isPresent()) {
			throw new CartNotPresentException();
		}

		Optional <Product> product = this.productRepository.findById(cartProduct.getId());
		
		if(!product.isPresent()) {
			throw new ProductNotPresentException();
		}
		if (cartProduct.getQuantity() == null) {
			throw new ProductQuantityRequiredException();
		}
		if (cartProduct.getQuantity() <= 0) {
			throw new ProductQuantityInvalidException();
		}
		if (product.get().getStock() < cartProduct.getQuantity()) {
			throw new ProductStockInsufficientException();
		}
		
		ProductInCart productInCart = new ProductInCart();
		productInCart.setProductId(cartProduct.getId());
		productInCart.setDescription(product.get().getDescription());
		productInCart.setUnitPrice(product.get().getUnitPrice());
		productInCart.setQuantity(cartProduct.getQuantity());
		
		Cart _cart = cart.get();
		productInCart.setCart(_cart);
		
		this.productInCartRepository.save(productInCart);	
		CartDto result = new CartDto();
		BeanUtils.copyProperties(_cart, result);
		return result;
	 }
	 
	 @Override
	 public ProductInCartDto DeleteProduct(long id, long productId) {
		 Optional<Cart> cart  = this.cartRepository.findById(id);
		 if (!cart.isPresent()) {
			 throw new CartNotPresentException();
		 }
		 System.out.println(cart.get().getStatus());
		 if (!cart.get().getStatus().equalsIgnoreCase("NEW")) {
			 throw new CartProcessingNotAllowDeletionException();
		 }
		 
		 ProductInCartDto result = new ProductInCartDto();
		 boolean exists = false;
		 for (ProductInCart product : cart.get().getProducts() ) {
			 if (product.getProductId() == productId) {
				 BeanUtils.copyProperties(product, result);
				 this.productInCartRepository.delete(product);
				 exists = true;
			 }
		 }
		 
		 if (!exists) {
			 throw new ProductNotPresentException();
		 }
			
		 return result;
	 }
	 
	 @Override
	 public Set<ProductInCartDto> GetCartProducts(long id) {
		 Optional<Cart> cart  = this.cartRepository.findById(id);
		 if (!cart.isPresent()) {
			 throw new CartNotPresentException();
		 }
		 /*
		 CartDto dto = new CartDto();
		 BeanUtils.copyProperties(cart.get(), dto);
		 
		 return dto.getProducts();
		 */
		 Set<ProductInCartDto> result = new HashSet<ProductInCartDto>();
		 
		 for (ProductInCart product : cart.get().getProducts() ) {
			 ProductInCartDto dto = new ProductInCartDto();
			 BeanUtils.copyProperties(product, dto);
			 
			 result.add(dto);
		 }
		 return result;
	 }
	 
	 @Override
	 public CartDto CheckoutCart(long id) {
		 Optional<Cart> cart  = this.cartRepository.findById(id);
		 if (!cart.isPresent()) {
			 throw new CartNotPresentException();
		 }
		 
		 Cart _cart = cart.get();
		 if (!_cart.getStatus().equalsIgnoreCase("NEW")) {
			 throw new CartStatusNotAllowCheckoutException();
		 }
		 _cart.setStatus("READY");
		 
		 this.cartRepository.save(_cart);
		 
		 CartDto dto = new CartDto();
		 BeanUtils.copyProperties(_cart, dto);
		 /*
		 for (ProductInCartDto productDto : dto.getProducts()) {
			 productDto.set
		 }
		 */
		 
		 return dto;
	 }
}

