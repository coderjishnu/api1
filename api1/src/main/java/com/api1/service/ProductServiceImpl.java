package com.api1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.api1.exception.ProductAlreadyPresentException;
import com.api1.exception.ProductNotDeletedException;
import com.api1.exception.ProductNotFoundException;
import com.api1.model.Product;
import com.api1.model.ResponseHandler;
import com.api1.model.Response;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
	private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Value("${get_product_by_id.url}")
	private String get_product_by_id_uri;

	@Value("${post_add_product.url}")
	private String post_add_product_uri;

	@Value("${post_update_product.url}")
	private String post_update_product_uri;

	@Value("${get_delete_product.url}")
	private String get_delete_product_uri;

	@Autowired
	WebClient.Builder webClientBuilder;

	public Response getProductById(String productId) throws ProductNotFoundException {
		log.info("Called getProductById service");

		ResponseHandler response = webClientBuilder.build().get().uri(get_product_by_id_uri, productId).retrieve()
				.bodyToMono(ResponseHandler.class).block();
		if (response.getResponseType().equals("FAILED")) {
			log.info("Throwed Product Not Found Exception");
			throw new ProductNotFoundException(response.getResponseMessage());
		}
		log.info("Product was found");
		log.info("Exited getProductById service");
		return this.getView(response);

	}

	public Product addProduct(Product product) throws ProductAlreadyPresentException {

		log.info("Called addProduct Service");
		ResponseHandler response = webClientBuilder.build().post().uri(post_add_product_uri).bodyValue(product)
				.retrieve().bodyToMono(ResponseHandler.class).block();
		if (response.getResponseType().equals("FAILED")) {
			log.info("Throwed Product Already Present Exception");
			throw new ProductAlreadyPresentException(response.getResponseMessage());
		}
		log.info("Product was Added");
		log.info("Exited addProduct service");
		return response.getProductResponse();
	}

	public Product updateProduct(Product product) throws ProductNotFoundException {
		log.info("Called updateProductService");
		ResponseHandler response = webClientBuilder.build().post().uri(post_update_product_uri).bodyValue(product)
				.retrieve().bodyToMono(ResponseHandler.class).block();
		if (response.getResponseType().equals("FAILED")) {
			log.info("Throwed Product Not Found Exception");
			throw new ProductNotFoundException(response.getResponseMessage());
		}
		log.info("Product was Updated");
		log.info("Exited updateProduct service");
		return response.getProductResponse();
	}

	public String deleteProduct(String productId) throws ProductNotDeletedException {
		log.info("Called delete product service");
		ResponseHandler response = webClientBuilder.build().get().uri(get_delete_product_uri, productId).retrieve()
				.bodyToMono(ResponseHandler.class).block();
		if (response.getResponseType().equals("FAILED")) {
			log.info("Throwed Product Not Deleted Exception");
			throw new ProductNotDeletedException(response.getResponseMessage());
		}
		log.info("Product is Deleted");
		log.info("Exited deleteProduct service");
		return response.getResponseMessage();

	}

	/**
	 * Method to get View for user from response with the status
	 * 
	 * @param response input the response
	 * @return view returns the view
	 */
	private Response getView(ResponseHandler response) {
		Response view = new Response();
		Product product = response.getProductResponse();
		view.setProduct(product);
		view.setStatus(response.getResponseMessage());
		return view;
	}

}
