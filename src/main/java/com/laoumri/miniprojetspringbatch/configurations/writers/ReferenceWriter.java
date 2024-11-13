package com.laoumri.miniprojetspringbatch.configurations.writers;

import com.laoumri.miniprojetspringbatch.entities.Reference;
import com.laoumri.miniprojetspringbatch.repositories.ReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReferenceWriter implements ItemWriter<Reference> {
    private final ReferenceRepository referenceRepository;
    @Override
    public void write(Chunk<? extends Reference> chunk) throws Exception {
        referenceRepository.saveAll(chunk);
    }
}
