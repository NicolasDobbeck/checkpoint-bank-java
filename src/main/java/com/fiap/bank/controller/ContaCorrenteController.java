package com.fiap.bank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fiap.bank.dto.PixDto;
import com.fiap.bank.dto.TransacaoDto;
import com.fiap.bank.model.ContaCorrente;


@RestController
@RequestMapping("/cc")
public class ContaCorrenteController {
    private List<ContaCorrente> repository = new ArrayList<>();
    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    @ResponseStatus()
    public ResponseEntity<?> stakeholders() {
        String resp = """
            Projeto Bank
            -------------------------------------
            Membros: 
            - José Bezerra Bastos Neto (RM559221)
            - Nicolas Dobbeck Mendes (RM5557605)
            """;
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/cadastro-conta")
    public ResponseEntity<ContaCorrente> create(@RequestBody ContaCorrente contaCorrente){
        log.info("Cadastrando... " + contaCorrente.getNome());
        repository.add(contaCorrente);
        return ResponseEntity.ok(contaCorrente);
    } 

    @GetMapping("/all")
    public List<ContaCorrente> getAll(){
        return repository;
    }

    @GetMapping("/{id}")
    public ContaCorrente getById(@PathVariable Long id){
        return findById(id);
     
    }

    @GetMapping("/cpf/{cpf}")
    public ContaCorrente getByCpf(@PathVariable String cpf){ 
        return findByCpf(cpf);
    }
    
    @PutMapping("/deposito")
    public ResponseEntity<ContaCorrente> deposito(@RequestBody TransacaoDto deposito){
        if(deposito.getIdConta() == null || deposito.getValor() <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Realizando deposito da conta..." + deposito.getIdConta());
        
        ContaCorrente contaCorrente = findById(deposito.getIdConta());
        
        if(contaCorrente == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        double novoSaldo = deposito.getValor() + contaCorrente.getSaldo();
        contaCorrente.setSaldo(novoSaldo);
        
        repository.remove(deposito.getIdConta());
        repository.add(contaCorrente);

        return ResponseEntity.ok(contaCorrente);
    }

    @PutMapping("/saque")
    public ResponseEntity<?> saque(@RequestBody TransacaoDto saque){
        if(saque.getIdConta() == null || saque.getValor() <= 0){
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "ID da conta é obrigatório e o valor do saque deve ser positivo."));
        }
        ContaCorrente contaCorrente = findById(saque.getIdConta());
        if(contaCorrente == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro","Conta não encontrada"));
        }
        if(saque.getValor() > contaCorrente.getSaldo()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("Bad erro","Sem saldo suficiente para realizar o  saque"));
        }
        double novoSaldo = contaCorrente.getSaldo() - saque.getValor();
        contaCorrente.setSaldo(novoSaldo);

        repository.remove(saque.getIdConta());
        repository.add(contaCorrente);
        return ResponseEntity.ok(contaCorrente);
    }

    @PutMapping("/pix")
    public ResponseEntity<?> pix(@RequestBody PixDto pix){
        
        if(pix.getIdOrigem() == null || pix.getIdDestino() == null || pix.getValor() <= 0){
            return ResponseEntity.badRequest().body(null);
        }
        ContaCorrente origem = findById(pix.getIdOrigem());
        ContaCorrente destino = findById(pix.getIdDestino());
        if (origem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("erro", "Conta de origem não encontrada."));
        }
        if (destino == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("erro", "Conta de destino não encontrada."));
        }
    
        if (pix.getValor() > origem.getSaldo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(Map.of("erro", "Saldo insuficiente na conta de origem para realizar a transferência."));
        }
    

        double novoSaldoOrigem = origem.getSaldo() - pix.getValor();
        double novoSaldoDestino = destino.getSaldo() + pix.getValor();
        origem.setSaldo(novoSaldoOrigem);
        destino.setSaldo(novoSaldoDestino);

        repository.remove(pix.getIdOrigem());
        repository.remove(pix.getIdDestino());
        repository.add(origem);
        repository.add(destino); 
        
        return ResponseEntity.ok(origem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ContaCorrente> delete(@PathVariable Long id){
        if(id == null){
            return ResponseEntity.badRequest().build();
        }

        ContaCorrente conta = findById(id);

        if(conta == null){
            return ResponseEntity.notFound().build();
        }
        conta.setStatus(false);
        
        repository.remove(conta);
        repository.add(conta);
        
        return ResponseEntity.noContent().build();

    }


    private ContaCorrente findByCpf(String cpf) {
        return repository.stream()
            .filter(c -> c.getCpf().replaceAll("[^0-9]", "").equals(cpf.replaceAll("[^0-9]", "")))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    

    private ContaCorrente findById(Long id) {
        return repository
                    .stream()
                    .filter(c -> c.getId().equals(id) && c.isActive())
                    .findFirst()
                    .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)
                    );
    }
    
}
