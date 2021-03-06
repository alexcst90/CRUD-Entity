package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.rest.ProductoService;
import com.example.demo.util.CustomErrorType;
import com.example.demo.entity.Producto;

@RestController
@RequestMapping("/producto")
public class ProductoController {
	
	public static final Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	ProductoService productoService;
	//Todos los productos
	//@RequestMapping(value="/productos/", method = RequestMethod.GET)
	@GetMapping("/productos/")
	public ResponseEntity<List<Producto>> listProductos(){
		
			List<Producto> productos = productoService.findAllProducto();
			if(productos.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}		
			return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);		
	}
	
	//Producto por id
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> getProducto(@PathVariable("id") Long id){
		
			logger.info("mostrando Producto con el id {} ", id);
			Producto prod = productoService.findById(id);	
		if(prod == null) {
			logger.error("Producto con el id {} no encontrado.", id);
			return new ResponseEntity(new CustomErrorType("Producto con el id " + id + " no encontrado"), HttpStatus.NOT_FOUND);			
		}
			return new ResponseEntity<Producto>(prod, HttpStatus.OK);			
	}
	
	//Insertar Producto
	
	@PostMapping("/productos/")
	public ResponseEntity<?> crearProducto(@RequestBody Producto producto, UriComponentsBuilder ucBuilder){
		
			logger.info("Insertando producto: {}", producto);
		if(productoService.isProductoExist(producto)){
			logger.error("No se puede crear. Producto con el nombre {} ya existe", producto.getNombreProducto());
			return new ResponseEntity(new CustomErrorType("No se puede crear. Producto con el nombre {}" + producto.getNombreProducto() + "ya existe."), HttpStatus.CONFLICT);
		}
		productoService.saveProduct(producto);
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/producto/productos/{id}").
        		buildAndExpand(producto.getIdproducto()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);		       
	}
	//Actualizar producto
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> updateProducto(@PathVariable("id") long id, @RequestBody Producto producto) {
		
			logger.info("Updating User with id {}", id);
        Producto currentProd = productoService.findById(id);
        if (currentProd == null) {
            logger.error("imposible actualizar. Producto with id {} not found.", id);
            return new ResponseEntity<Object>(new CustomErrorType("Unable to upate. Producto with id " + id + " not found."),HttpStatus.NOT_FOUND);
        }
        currentProd.setNombreProducto(producto.getNombreProducto());
        currentProd.setDescripcion(producto.getDescripcion());
        currentProd.setPrecioLista(producto.getPrecioLista());
        currentProd.setStock(producto.getStock());
        productoService.updateProducto(currentProd);
        return new ResponseEntity<Producto>(currentProd, HttpStatus.OK);
	}
	//Eliminar Producto
	@DeleteMapping("/productos/{id}")
	public ResponseEntity<?> deleteProducto(@PathVariable("id") long id){
		
			logger.info("Deleting Producto with id {}", id);
			Producto currentProduct = productoService.findById(id);
		if(currentProduct == null) {
			logger.error("imposible eliminar. Producto with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Imposible eliminar. Producto with id " + id + " not found."),HttpStatus.NOT_FOUND);
		}
			productoService.deleteProducto(currentProduct);
			return new ResponseEntity<Producto>(currentProduct, HttpStatus.OK);
	}
	
	
	
} 
