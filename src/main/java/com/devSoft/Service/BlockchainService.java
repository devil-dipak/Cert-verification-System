package com.devSoft.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devSoft.Model.Block;
import com.devSoft.Repository.BlockRepository;
import com.devSoft.Utils.HashUtil;


@Service
public class BlockchainService {

    @Autowired
    private BlockRepository blockRepo;

    public Block saveCertificateHash(String certificateHash) {

        List<Block> blocks = blockRepo.findAll();
        String previousHash = "0";
        long timestamp = System.currentTimeMillis();

        if (!blocks.isEmpty()) {
            previousHash = blocks.get(blocks.size() - 1).getCurrentHash();
        }

        Block block = new Block();
        block.setData(certificateHash);
        block.setPreviousHash(previousHash);
        block.setTimestamp(timestamp);

        String currentHash = HashUtil.sha256(
                certificateHash + previousHash + timestamp);

        block.setCurrentHash(currentHash);

        return blockRepo.save(block);
    }

    public boolean isBlockchainValid() {

        List<Block> blocks = blockRepo.findAll();

        // 1. Verify each block's own hash integrity
        for (int i = 0; i < blocks.size(); i++) {

            Block block = blocks.get(i);

            String expectedHash = HashUtil.sha256(
                    block.getData() + block.getPreviousHash() + block.getTimestamp());

            if (!expectedHash.equals(block.getCurrentHash())) {
                return false;
            }
        }

        // 2. Verify chain linkage (genesis block has previousHash = "0")
        for (int i = 1; i < blocks.size(); i++) {

            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i - 1);

            if (!currentBlock.getPreviousHash()
                    .equals(previousBlock.getCurrentHash())) {

                return false;
            }
        }

        return true;
    }
}