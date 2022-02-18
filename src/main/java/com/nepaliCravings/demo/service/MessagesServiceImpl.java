package com.nepaliCravings.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nepaliCravings.demo.model.Messages;
import com.nepaliCravings.demo.repository.MessagesRepository;

@Service
public class MessagesServiceImpl implements MessagesService {
	@Autowired
	private MessagesRepository messagesRepository;

	@Override
	public Messages findByRestaurantId(long id) {
		return messagesRepository.findByRestaurantId(id);
	}

	@Override
	public Messages findById(long id) {
		return messagesRepository.findById(id);
	}

	@Override
	public List<Messages> findAll() {
		return messagesRepository.findAll();
	}

	@Override
	public void save(Messages messages) {
		messagesRepository.save(messages);
		
	}

	@Override
	public void delete(Messages message) {
		
		messagesRepository.delete(message);
	}

	@Override
	public Messages findByUserId(long id) {
		// TODO Auto-generated method stub
		return messagesRepository.findByUserId(id);
	}

	

}
