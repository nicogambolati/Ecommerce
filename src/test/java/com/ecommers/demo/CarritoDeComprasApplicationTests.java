package com.ecommers.demo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ecommers.exceptions.CartNotPresentException;
import com.ecommers.repository.ICartRepository;
import com.ecommers.service.CartService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarritoDeComprasApplicationTests {
	

	@Autowired
	private CartService cartService;
	
	@Autowired
	private ICartRepository cartRepo;

	
	public CarritoDeComprasApplicationTests() {
	}

	
    @Before
    public void seEjecutaAntesDeCadaTest() {
    }

    @BeforeClass
    public static void seEjecutaUnaSolaVezAlPrincipio() {
    }
    
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
    
//    @Test()
//    public void testCartSearch() {
//    	
//    	Cart carrito = new Cart();
//    	carrito.setEmail("gp@gmail.com");
//    	carrito.setFullName("German Pesce");
//    	carrito.setStatus("NEW");
//    	carrito.setTotal(BigDecimal.ZERO);
//    	
//    	cartRepo.save(carrito);
//    	assertNotNull(cartService.getCartByEmail("gp@gmail.com"));
//    }
    
    
    @Test
	public void cartCheckoutWithInvalidIdThrowException() {
	    exceptionRule.expect(CartNotPresentException.class);
	    cartService.CheckoutCart(77777L);
	}
    
    @After
    public void seEjecutaDespuesDeCadaTest() {
    }
 
    @AfterClass
    public static void seEjecutaUnaSolaVezAlFinal() {
    }
	
}
