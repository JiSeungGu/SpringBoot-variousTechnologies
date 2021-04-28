package net.sejongtelecom.backendapi.domain.token;

import java.util.Optional;
import net.sejongtelecom.backendapi.common.entity.Wallet;
import net.sejongtelecom.backendapi.common.repository.WalletJpaRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TokenTests {

  @Autowired
  private WalletJpaRepo walletJpaRepo;

  @DisplayName("Selecting Private key test")
  @Test
  public void testSelectingPrivateKeyFindByWaddress() {

    String waddress = "BV8oLGiPd8qTGsEqqJ7b9V7X29VuQcea2C";
    String tempKey = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCBdpd/sDSSs0LCM+4TMHnVyYSWKsNt8SGeSsoIxYjU/Ow==";

    Optional<Wallet> optionalWallet = walletJpaRepo.findByWaddress(waddress);

    String privateKey = null;

    if (optionalWallet.isPresent()){
      Wallet wallet = optionalWallet.get();
      privateKey = wallet.getPrivatekey();
    }

    assertNotNull(privateKey);
    assertEquals(privateKey, tempKey);

  }

  @DisplayName("Selecting Private key test")
  @Test
  public void testSelectingPrivateKeyFindByUserid() {

    String userId = "8";
    String tempKey = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCBdpd/sDSSs0LCM+4TMHnVyYSWKsNt8SGeSsoIxYjU/Ow==";

    Optional<Wallet> optionalWallet = walletJpaRepo.findByUserid(userId);

    String privateKey = null;

    if (optionalWallet.isPresent()){
      Wallet wallet = optionalWallet.get();
      privateKey = wallet.getPrivatekey();
    }

    assertNotNull(privateKey);
    assertEquals(privateKey, tempKey);

  }

}

