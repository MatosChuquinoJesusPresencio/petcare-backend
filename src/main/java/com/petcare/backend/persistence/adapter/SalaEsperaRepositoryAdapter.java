package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.port.SalaEsperaRepositoryPort;
import com.petcare.backend.persistence.mapper.SalaEsperaMapper;
import com.petcare.backend.persistence.repository.SalaEsperaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SalaEsperaRepositoryAdapter implements SalaEsperaRepositoryPort {

    private final SalaEsperaRepository salaEsperaRepository;
    private final SalaEsperaMapper salaEsperaMapper;

    public SalaEsperaRepositoryAdapter(SalaEsperaRepository salaEsperaRepository,
                                        SalaEsperaMapper salaEsperaMapper) {
        this.salaEsperaRepository = salaEsperaRepository;
        this.salaEsperaMapper = salaEsperaMapper;
    }

    @Override
    public SalaEspera save(SalaEspera salaEspera) {
        var entity = salaEsperaMapper.toEntity(salaEspera);
        var saved = salaEsperaRepository.save(entity);
        return salaEsperaMapper.toModel(saved);
    }

    @Override
    public Optional<SalaEspera> findById(Long id) {
        return salaEsperaRepository.findById(id).map(salaEsperaMapper::toModel);
    }

    @Override
    public Optional<SalaEspera> findByCitaId(Long citaId) {
        return salaEsperaRepository.findByCitaId(citaId).map(salaEsperaMapper::toModel);
    }

    @Override
    public List<SalaEspera> findAllByOrderByFechaLlegadaAsc() {
        return salaEsperaRepository.findAllByOrderByFechaLlegadaAsc().stream()
                .map(salaEsperaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalaEspera> findByEstadoOrderByFechaLlegadaAsc(String estado) {
        return salaEsperaRepository.findByEstadoOrderByFechaLlegadaAsc(estado).stream()
                .map(salaEsperaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        salaEsperaRepository.deleteById(id);
    }
}
