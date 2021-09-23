package com.esoft.placemaps.placemaps.evento;

import com.esoft.placemaps.placemaps.pedidocadastro.PedidoCadastro;
import com.esoft.placemaps.placemaps.pedidocadastro.PedidoCadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/evento")
public class EventoPublicController {

  private final EventoService eventoService;

  @Autowired
  public EventoPublicController(EventoService eventoService) {
    this.eventoService = eventoService;
  }

  @GetMapping
  public ResponseEntity<Page<Evento>> obterEventos(Pageable pageable,
                                                   @RequestParam(value = "nome", required = false, defaultValue = "") String nome) {
    return ResponseEntity.ok(this.eventoService.obterEventos(pageable, nome));
  }

}