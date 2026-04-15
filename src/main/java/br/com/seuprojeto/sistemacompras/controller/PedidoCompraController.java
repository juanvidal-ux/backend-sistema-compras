package br.com.seuprojeto.sistemacompras.controller;

import br.com.seuprojeto.sistemacompras.dto.PedidoResumoDTO;
import br.com.seuprojeto.sistemacompras.model.PedidoCompra;
import br.com.seuprojeto.sistemacompras.repository.PedidoCompraRepository;
import br.com.seuprojeto.sistemacompras.service.AuditoriaService;
import br.com.seuprojeto.sistemacompras.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(
    origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500",
        "https://juanvidal-ux.github.io"
    },
    methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
    },
    allowedHeaders = "*"
)
public class PedidoCompraController {

    @Autowired
    private PedidoCompraRepository pedidoRepository;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping("/anos-distintos")
    public List<Integer> getAnosDistintos() {
        return pedidoRepository.findDistinctAnos();
    }

    @GetMapping("/documento/{id}")
    public ResponseEntity<byte[]> gerarDocx(@PathVariable Integer id) {
        try {
            Optional<PedidoCompra> pedidoOpt = pedidoRepository.findByIdCompleto(id);

            if (pedidoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            auditoriaService.registrar("BAIXAR_DOC", "PCN: " + pedidoOpt.get().getCodigoPcn());

            byte[] relatorio = documentoService.gerarDocumentoWord(pedidoOpt.get());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=PCN_" + pedidoOpt.get().getCodigoPcn() + ".docx")
                    .header(HttpHeaders.CONTENT_TYPE,
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .body(relatorio);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public List<PedidoResumoDTO> listarPedidos() {
        return pedidoRepository.listarResumo();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoCompra> getPedidoPorId(@PathVariable Integer id) {
        Optional<PedidoCompra> pedido = pedidoRepository.findByIdCompleto(id);
        return pedido.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PedidoCompra criarPedido(@RequestBody PedidoCompra pedido) {
        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> item.setPedidoCompra(pedido));
        }

        PedidoCompra salvo = pedidoRepository.save(pedido);

        auditoriaService.registrar("CRIAR_PEDIDO", "Novo PCN: " + salvo.getCodigoPcn());

        return salvo;
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoCompra> atualizarPedido(@PathVariable Integer id, @RequestBody PedidoCompra pedido) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedido.setId(id);

        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> item.setPedidoCompra(pedido));
        }

        PedidoCompra salvo = pedidoRepository.save(pedido);

        auditoriaService.registrar("EDITAR_PEDIDO", "Alterado PCN: " + salvo.getCodigoPcn());

        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPedido(@PathVariable Integer id) {
        Optional<PedidoCompra> pedido = pedidoRepository.findById(id);

        if (pedido.isPresent()) {
            String pcn = pedido.get().getCodigoPcn();

            pedidoRepository.deleteById(id);

            auditoriaService.registrar("EXCLUIR_PEDIDO", "Removido PCN: " + pcn);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
