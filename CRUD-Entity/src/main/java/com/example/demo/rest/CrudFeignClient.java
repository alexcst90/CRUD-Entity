package com.example.demo.rest;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value= "/products/")
public interface CrudFeignClient extends ProductoService {

}
