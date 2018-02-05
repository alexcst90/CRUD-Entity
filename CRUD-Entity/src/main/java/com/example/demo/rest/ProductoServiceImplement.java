package com.example.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Producto;
import com.example.demo.jdbc.ProductoDAOImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service("productoService")
public class ProductoServiceImplement implements ProductoService {
	
	@Autowired 
	ProductoDAOImpl productDao;
	
	@HystrixCommand(fallbackMethod = "findAllProducto")
	public List<Producto> findAllProducto() { 		
		// TODO Auto-generated method stub
		
		return productDao.findAllProducto();
	}
	

	@HystrixCommand(fallbackMethod = "findById")
	public Producto findById(Long id){
		return productDao.findById(id); 
	}

	
	public boolean isProductoExist(Producto producto) {
		// TODO Auto-generated method stub
		return findById(producto.getIdproducto())!=null;
	}

	@HystrixCommand(fallbackMethod = "saveProduct")
	public boolean saveProduct(Producto producto) {
		// TODO Auto-generated method stub
		
		return productDao.saveProduct(producto);		
	}

	@HystrixCommand(fallbackMethod = "updateProducto")
	public boolean updateProducto(Producto producto) {
		// TODO Auto-generated method stub
		return productDao.updateProducto(producto);
	}

	@HystrixCommand(fallbackMethod = "deleteProducto")
	public boolean deleteProducto(Producto producto) {
		// TODO Auto-generated method stub
		return productDao.deleteProducto(producto);
	}
	
	
}
