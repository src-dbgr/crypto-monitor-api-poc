package com.sam.coin.api.controller;

import com.sam.coin.model.DynamicEntity;
import com.sam.coin.service.DynamicCandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/coincandle/dynamic")
public class DynamicCoinCandleController {
    private final DynamicCandleService dynamicCandleService;

    @Autowired
    public DynamicCoinCandleController(DynamicCandleService dynamicCandleService) {
        this.dynamicCandleService = dynamicCandleService;
    }

    @PostMapping
    public DynamicEntity create(@RequestBody Object data) {
        DynamicEntity dynamicEntity = new DynamicEntity();
        dynamicEntity.setData(data);
        return dynamicCandleService.save(dynamicEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Optional<DynamicEntity> dynamicEntity = dynamicCandleService.findById(id);

        if (dynamicEntity.isPresent()) {
            return ResponseEntity.ok(dynamicEntity.get().getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
