package com.ecommers.exceptions;

import org.springframework.http.HttpStatus;

public class CartStatusNotAllowCheckout extends BaseECommersException{
	private static final long serialVersionUID = 3283052790426003761L;

	public CartStatusNotAllowCheckout() {
		super (HttpStatus.CONFLICT, "CART_STATUS_NOT_ALLOW_CHECKOUT", "Cart status not allow checkout");
	}

}
