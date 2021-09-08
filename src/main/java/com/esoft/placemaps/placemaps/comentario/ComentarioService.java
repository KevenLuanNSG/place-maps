package com.esoft.placemaps.placemaps.comentario;

import com.esoft.placemaps.placemaps.comentario.dto.ComentarioFormDTO;
import com.esoft.placemaps.placemaps.comentario.exception.ComentarioBadRequestException;
import com.esoft.placemaps.placemaps.ponto.Ponto;
import com.esoft.placemaps.placemaps.ponto.PontoRepository;
import com.esoft.placemaps.placemaps.usuario.UsuarioEscopo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PontoRepository pontoRepository;

    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository,
                             PontoRepository pontoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.pontoRepository = pontoRepository;
    }

    @Transactional
    public Comentario salvar(ComentarioFormDTO comentarioFormDTO) {
        Comentario comentario = comentarioFormDTO.gerarComentario();
        Optional<Ponto> pontoOptional = this.pontoRepository.findById(comentarioFormDTO.getPontoId());
        if (pontoOptional.isPresent()) {
            comentario.setPonto(pontoOptional.get());
            comentario.setUsuario(UsuarioEscopo.usuarioAtual());
            return this.comentarioRepository.save(comentario);
        }
        throw new ComentarioBadRequestException("Ponto não encontrado.");
    }

}
