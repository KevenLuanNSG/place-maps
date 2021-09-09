package com.esoft.placemaps.placemaps.localizacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import com.esoft.placemaps.placemaps.diadasemana.DiaDaSemana;
import com.esoft.placemaps.placemaps.diadasemana.DiaDaSemanaRepository;
import com.esoft.placemaps.placemaps.localizacao.dto.LocalizacaoFormDTO;
import com.esoft.placemaps.placemaps.localizacao.exception.LocalizacaoBadRequestException;
import com.esoft.placemaps.placemaps.ponto.Ponto;
import com.esoft.placemaps.placemaps.ponto.PontoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoService {

    private final LocalizacaoRepository localizacaoRepository;

    private final PontoRepository pontoRepository;

    private final DiaDaSemanaRepository diaDaSemanaRepository;

    @Autowired
    public LocalizacaoService(LocalizacaoRepository localizacaoRepository,
                              PontoRepository pontoRepository,
                              DiaDaSemanaRepository diaDaSemanaRepository) {

        this.localizacaoRepository = localizacaoRepository;
        this.pontoRepository = pontoRepository;
        this.diaDaSemanaRepository = diaDaSemanaRepository;
    }

    @Transactional
    public Localizacao salvar(LocalizacaoFormDTO localizacaoFormDTO) {
        Localizacao localizacao = localizacaoFormDTO.gerarLocalizacao();
        Optional<Ponto> pontoOptional = pontoRepository.findById(localizacaoFormDTO.getPontoId());
        if (!pontoOptional.isPresent()) {
            throw new LocalizacaoBadRequestException("Ponto não encontrado.");
        }
        localizacao.setPonto(pontoOptional.get());
        List<DiaDaSemana> diasDaSemana = new ArrayList<>();
        for (String idDiaDaSemana : localizacaoFormDTO.getDiasDaSemanaIds()){
            Optional<DiaDaSemana> diaDaSemanaOptional = diaDaSemanaRepository.findById(idDiaDaSemana);
            if (diaDaSemanaOptional.isPresent()) {
                diasDaSemana.add(diaDaSemanaOptional.get());
            }
        }
        if (diasDaSemana.size() != localizacaoFormDTO.getDiasDaSemanaIds().size()) {
            throw new LocalizacaoBadRequestException("Algum(ns) DiaDaSemana não foi encontrado.");
        }
        localizacao.setDiasDaSemana(diasDaSemana);
        return localizacaoRepository.save(localizacao);
    }

    @Transactional
    public Localizacao obterPorPontoEDiaDaSemana(String pontoId, String nomeDiaSemana) {
        String localizacaoId = this.localizacaoRepository.obterPorPontoEDiaDaSemana(pontoId, nomeDiaSemana);
        if (Objects.isNull(localizacaoId)) {
            throw new LocalizacaoBadRequestException("Nenhuma localização encontrada nesse dia da semana para esse ponto.");
        }
        return this.localizacaoRepository.findById(localizacaoId).get();
    }
    
}
