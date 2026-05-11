package com.devSoft.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devSoft.Model.Block;
import com.devSoft.Repository.BlockRepository;
import com.devSoft.Service.BlockService;

@Service
public class BlockServiceImpl implements BlockService {

	@Autowired
	private BlockRepository blcRepo;

	@Override
	public void addBlc(Block blc) {

		blcRepo.save(blc);

	}

	@Override
	public void deleteBlc(Long id) {

		blcRepo.deleteById(id);

	}

	@Override
	public void updateBlc(Block blc) {

		blcRepo.save(blc);

	}

	@Override
	public Block getBlcById(Long id) {

		return blcRepo.findById(id).orElseThrow(() -> new RuntimeException("Block not found with id:" + id));
	}

	@Override
	public List<Block> getAllBlcs() {

		return blcRepo.findAll();
	}
	
	@Override
	public boolean isChainValid() {

	    java.util.List<com.devSoft.Model.Block> blocks = blcRepo.findAll();

	    // 1. Verify each block's own hash integrity
	    for (int i = 0; i < blocks.size(); i++) {

	        com.devSoft.Model.Block block = blocks.get(i);

	        // Try new format (with timestamp)
	        String expectedHash = com.devSoft.Utils.HashUtil.sha256(
	                block.getData() + block.getPreviousHash() + block.getTimestamp());

	        if (!expectedHash.equals(block.getCurrentHash())) {
	            // Try legacy format (without timestamp) for manually created blocks
	            String legacyHash = com.devSoft.Utils.HashUtil.sha256(
	                    block.getData() + block.getPreviousHash());
	            if (!legacyHash.equals(block.getCurrentHash())) {
	                return false;
	            }
	        }
	    }

	    // 2. Verify chain linkage
	    for (int i = 1; i < blocks.size(); i++) {

	        com.devSoft.Model.Block current = blocks.get(i);
	        com.devSoft.Model.Block previous = blocks.get(i - 1);

	        if (!current.getPreviousHash().equals(previous.getCurrentHash())) {
	            return false;
	        }
	    }

	    return true;
	}

}
