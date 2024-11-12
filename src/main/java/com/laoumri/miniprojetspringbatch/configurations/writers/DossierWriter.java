package com.laoumri.miniprojetspringbatch.configurations.writers;

import com.laoumri.miniprojetspringbatch.entities.Dossier;
import com.laoumri.miniprojetspringbatch.repositories.DossierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DossierWriter implements ItemWriter<Dossier> {
    private final DossierRepository dossierRepository;
    @Override
    public void write(Chunk<? extends Dossier> chunk) throws Exception {
        dossierRepository.saveAll(chunk);
    }
}
