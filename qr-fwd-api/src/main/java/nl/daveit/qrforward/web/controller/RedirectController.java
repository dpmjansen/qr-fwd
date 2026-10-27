package nl.daveit.qrforward.web.controller;

import jakarta.validation.Valid;
import nl.daveit.qrforward.domain.api.RedirectApi;
import nl.daveit.qrforward.domain.model.RedirectKey;
import nl.daveit.qrforward.web.mapper.RedirectConfigurationMapper;
import org.openapitools.model.RedirectConfigurationRequest;
import org.openapitools.model.RedirectConfigurationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/redirect")
public class RedirectController {

    private final RedirectApi redirectApi;
    private final RedirectConfigurationMapper mapper;

    public RedirectController(RedirectApi redirectApi, RedirectConfigurationMapper mapper) {
        this.redirectApi = redirectApi;
        this.mapper = mapper;
    }

    @GetMapping("/configurations")
    public ResponseEntity<List<RedirectConfigurationResponse>> getConfigurations(){
        List<RedirectConfigurationResponse> configs = redirectApi.getAllRedirectConfigurations().stream().map(mapper::fromDomain).toList();
        return ResponseEntity.ok(configs);
    }

    @PostMapping("/configurations")
    public ResponseEntity<Void> addConfiguration(@Valid @RequestBody RedirectConfigurationRequest request){
        var domain = mapper.toDomain(request);
        redirectApi.addNewConfiguration(domain);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RedirectConfigurationResponse> getConfiguration(@PathVariable UUID uuid){
        var web = redirectApi.getRedirectConfiguration(new RedirectKey(uuid)).map(mapper::fromDomain);
        return ResponseEntity.of(web);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> getConfigurations(@PathVariable UUID uuid, @RequestBody RedirectConfigurationRequest request){
        if(uuid != request.getUuid()) {
            return ResponseEntity.status(409).build();
        }
        redirectApi.updateRedirectConfiguration(mapper.toDomain(request));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> getConfigurations(@PathVariable UUID uuid){
        redirectApi.deleteRedirectConfiguration(new RedirectKey(uuid));
        return ResponseEntity.ok().build();
    }
}
