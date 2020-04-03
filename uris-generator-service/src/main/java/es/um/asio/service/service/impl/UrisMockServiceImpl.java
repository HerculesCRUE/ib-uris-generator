package es.um.asio.service.service.impl;

import org.springframework.stereotype.Service;

import es.um.asio.service.service.UrisMockService;

@Service
public class UrisMockServiceImpl implements UrisMockService {
	public String getUri() {
		return "cool uri";
	}
}
