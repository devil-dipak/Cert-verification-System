package com.devSoft.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devSoft.Model.Block;

@Service
public interface BlockService {
	
	void addBlc(Block blc);
	void deleteBlc(Long id);
	void updateBlc(Block blc);
	Block getBlcById(Long id);
	List<Block> getAllBlcs();
	boolean isChainValid();

}