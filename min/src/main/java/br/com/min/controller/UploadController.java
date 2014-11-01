package br.com.min.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.ImagemDAO;
import br.com.min.entity.Imagem;

@Controller
@RequestMapping("/upload")
public class UploadController {
	
	@Autowired
	private GenericDAO dao;
	@Autowired
	private ImagemDAO imagemDao;
	
	@RequestMapping("/")
	public @ResponseBody Long upload(@RequestParam("imagem") MultipartFile arquivo){
		Imagem imagem = new Imagem();
		try {
			imagem.setImagem(arquivo.getBytes());
			imagem = (Imagem)dao.persist(imagem);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return imagem.getId();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<byte[]> find(@PathVariable("id") Long id){
		Imagem imagem = imagemDao.findById(id);
		if(imagem != null && imagem.getImagem() == null){
			imagem = imagemDao.findById(1L);
		}
		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    
		return new ResponseEntity<byte[]>(imagem.getImagem(), headers, HttpStatus.OK);
	}
	
}
