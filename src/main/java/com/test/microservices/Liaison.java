package com.test.microservices;

import java.util.List;

import org.springframework.stereotype.Component;

import com.test.microservices.pojos.*;
import com.test.microservices.repositories.*;

@Component
public class Liaison {
	VideoRepository srcRepo;
	TechniqueRepository destRepo;
	public Liaison(VideoRepository s, TechniqueRepository dest) {
		this.srcRepo = s;
		this.destRepo = dest;
	}
	public void creer() {
		System.out.println("Creation de liens entre Video et Technique2 en cours...");

		int trouves=0;
		int nontrouves=0;
		List<Video>liste=srcRepo.findAll();
		for(Video element:liste) {
			if(destRepo.existsById(element.getTechnique2_id())) {
				Technique c=destRepo.findById(element.getTechnique2_id());
				element.setTechnique2(c);
				trouves++;
			}else{
				element.setTechnique2(null);
				nontrouves++;
			}
			srcRepo.save(element);
		}
		System.out.println("Creation de liens entre Video et Technique2 finie trouves: "+trouves+" non trouves: "+nontrouves+"...");
	}
}
