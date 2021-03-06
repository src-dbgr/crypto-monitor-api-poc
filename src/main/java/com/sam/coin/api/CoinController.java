package com.sam.coin.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.coin.model.Coin;
import com.sam.coin.service.CoinService;

@RequestMapping("/api/v1/coin")
@RestController
public class CoinController {
	private static final Logger LOG = LoggerFactory.getLogger(CoinController.class);
	private final CoinService coinService;

	@Autowired
	public CoinController(CoinService coinService) {
		this.coinService = coinService;
	}

	@PostMapping
	public void addCoin(@Valid @NonNull @RequestBody Coin coin) {
		coinService.addCoin(coin);
		LOG.info("Added new coin: " + coin.getSymbol());
	}

	@GetMapping
	public List<Coin> getAllCoins() {
		//TODO
		return null;
//		return coinService.getAllCoins();
	}

	@DeleteMapping(path = "{id}")
	public void deleteCoin(@PathVariable("id") UUID id) {
		coinService.deleteCoinByID(id);
	}

	@GetMapping(path = "{id}")
	public Optional<Coin> getCoinById(@PathVariable("id") UUID id) {
		return coinService.getCoinByID(id);
	}

	@PutMapping(path = "{id}")
	public void updateCoin(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Coin coinToUpdate) {
		LOG.debug("PUT ID: " + id);
		coinService.updateCoin(id, coinToUpdate);
	}

}
