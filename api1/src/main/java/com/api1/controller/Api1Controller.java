package com.api1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.api1.exception.ProductAlreadyPresentException;
import com.api1.exception.ProductNotDeletedException;
import com.api1.exception.ProductNotFoundException;
import com.api1.model.Product;
import com.api1.model.Api1Response;
import com.api1.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * Api1Controller Class. Contains all the Handler Methods
 *
 */
@RestController
@Api
@RequestMapping("/api1")
public class Api1Controller {

	@Autowired
	ProductService serv;

	/**
	 * Returns a response for productId.
	 * 
	 * @param productId
	 * @return response
	 */
	@GetMapping("/search/{productId}")
	@ApiOperation(value = "Search by Product ID")
	public ResponseEntity<Api1Response> getProductById(@PathVariable String productId) {
		try {
			System.out.println("=========getProductTry=========");
			System.out.println(serv.getProductById(productId));
			return new ResponseEntity<Api1Response>(serv.getProductById(productId), HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			Api1Response response = new Api1Response();
			response.setProduct(null);
			response.setStatus(e.getMessage());
			System.out.println("=========getProductCatch=========");
			return new ResponseEntity<Api1Response>(response, HttpStatus.OK);
		}
	}

	/**
	 * Saves the product and returns the response containing the added product if it
	 * was added.
	 * 
	 * @param product
	 * @return response
	 */
	@PostMapping("/add")
	@ApiOperation(value = "Add Product")
	public ResponseEntity<Api1Response> addProduct(@RequestBody @Valid Product product) {

		try {
			System.out.println("=========addProductTry=========");
			System.out.println(product);
			System.out.println(serv.addProduct(product));
			return new ResponseEntity<Api1Response>(serv.addProduct(product), HttpStatus.OK);
		} catch (ProductAlreadyPresentException e) {
			Api1Response response = new Api1Response();
			response.setProduct(null);
			response.setStatus(e.getMessage());
			System.out.println("=========addProductCatch=========");
			return new ResponseEntity<Api1Response>(response, HttpStatus.OK);
		}

	}

	/**
	 * Updates the product if the product was present and returns the response
	 * containing updated product.
	 * 
	 * @param product
	 * @return response
	 */
	@PostMapping("/update")
	@ApiOperation(value = "Update Product")
	public ResponseEntity<Api1Response> updateProduct(@RequestBody @Valid Product product) {
		try {
			System.out.println("========updateProductTry==========");
			System.out.println(serv.updateProduct(product));
			return new ResponseEntity<Api1Response>(serv.updateProduct(product), HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			Api1Response response = new Api1Response();
			response.setProduct(null);
			response.setStatus(e.getMessage());
			System.out.println("========updateProductCatch==========");
			return new ResponseEntity<Api1Response>(response, HttpStatus.OK);
		}
	}

	/**
	 * Deletes the product by Id and returns the message.
	 * 
	 * @param productId
	 * @return string
	 */
	@GetMapping("/delete/{productId}")
	@ApiOperation(value = "Delete Product")
	public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
		try {
			System.out.println("========deleteProductTry==========");
			System.out.println(serv.deleteProduct(productId));
			return new ResponseEntity<String>(serv.deleteProduct(productId), HttpStatus.OK);
		} catch (ProductNotDeletedException e) {
			System.out.println("========deleteProductCatch==========");
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

}
