package net.sejongtelecom.backendapi.common.fabric.sdk.wallet;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import net.sejongtelecom.backendapi.common.dto.WalletDto;
import net.sejongtelecom.backendapi.common.entity.WalletTransactionSend;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.json.simple.JSONObject;

@Slf4j
public class MakeWallet {

  public static String getWalletParams(WalletTransactionSend walletTransactionSend) {

//    ApiSend vaildParam = new ApiSend();
//    vaildParam.setCodenm("vaildWallet");
//    vaildParam.setFuncnm("getNonce");
//    vaildParam.setParams(apiSend.getWaddress());
//    HashMap<Object, Object> nonceMap = FabApiConnect.sendFabApi(vaildParam, "query");
//    String strNonce = nonceMap.get("value").toString();
//    long nonce = 0;
//    try {
//      long nowNonce = Long.parseLong(strNonce);
//      nonce = nowNonce + 1;
//      apiSend.setNonce(nonce);
//    } catch (Exception e) {
//      log.error(e.toString());
//    }

    walletTransactionSend.setPrivatekey(walletTransactionSend.getPrivatekey());

    // NONCE 값을 포함한 Transation Parameter 생성
    String transParam =
        makeTransationParam(walletTransactionSend);

    return transParam;
  }

  private static String makeTransationParam(WalletTransactionSend walletTransactionSend) {

    String transParam = "";

    try {
      PrivateKey privateKey = base64ToPrivateKey(walletTransactionSend.getPrivatekey());
      PublicKey publicKey = getPublicKey(privateKey);
      String pubHex = getPublicKeyAsHex(publicKey);

      String transdata = walletTransactionSend.getParams();

      long txtime = Instant.now().getEpochSecond();
      //String callfunc = apiSend.getFuncnm();

      String sigOrgMsg = pubHex + txtime;
      String shaSigOrgMsg = SHA2.getSHA256(sigOrgMsg);

      byte[] signature = signMsg(shaSigOrgMsg, privateKey);

      JSONObject paramJson = new JSONObject();
      paramJson.put("publickey", pubHex);
      paramJson.put("txtime", txtime + "");
      paramJson.put("transdata", transdata);
      paramJson.put("sigmsg", bytesToHex(signature));
      //paramJson.put("callfunc", callfunc);
      //paramJson.put("nonce", String.valueOf(apiSend.getNonce()));

      transParam = paramJson.toJSONString();

      log.info("SEND PARAM:" + transParam);

    } catch (Exception e) {
    }

    return transParam;
  }

  public static WalletDto makeNewPrivateKey(WalletDto walletDto) {

    try {
      KeyData newKey = generateKey();

      walletDto.setPrivatekey(newKey.getPrivateKey());

      PrivateKey privateKey = base64ToPrivateKey(newKey.getPrivateKey());
      PublicKey publicKey = base64ToPublicKey(newKey.getPublicKey());

      String privHex = getPrivateKeyAsHex(privateKey);
      String pubHex = getPublicKeyAsHex(publicKey);

      // --PublicKey Double SHA256
      String shaPubKey = SHA2.getSHA256(SHA2.getSHA256(pubHex));

      shaPubKey = getRipemd160(shaPubKey);
      BigInteger shaPubKeyInt = new BigInteger(shaPubKey, 32);

      String walletAddr = Base58CheckEncoding.convertToBase58(shaPubKeyInt + "", 10);

      walletDto.setWaddress(walletAddr);

    } catch (Exception e) {
      log.error("PRIVATEKEY MAKE ERR:" + e);
    }
    return walletDto;
  }

  private static String getRipemd160(String messageText) {

    String digestStr = "";

    try {
      byte[] r = messageText.getBytes("UTF-8");

      RIPEMD160Digest d = new RIPEMD160Digest();
      d.update(r, 0, r.length);

      byte[] o = new byte[d.getDigestSize()];
      d.doFinal(o, 0);

      // Hex.encode(o, System.out);
      // System.out.println(); // 45147c708948188cead54a10b95899a36f47dc9c

      // String digestStr = Hex.toHexString(o);
      org.apache.commons.io.output.ByteArrayOutputStream byteArrayOutputStream =
          new org.apache.commons.io.output.ByteArrayOutputStream();
      Hex.encode(o, byteArrayOutputStream);
      digestStr = byteArrayOutputStream.toString("UTF8");
      // System.out.println(digestStr); // 45147c708948188cead54a10b95899a36f47dc9c
    } catch (Exception e) {
      System.out.println(e.toString());
    }

    return digestStr;
  }

  private static boolean verifySignature(PublicKey pubKey, String msg, byte[] signature)
      throws Exception {
    byte[] message = msg.getBytes("UTF-8");
    Signature ecdsa = Signature.getInstance("SHA1withECDSA");

    ecdsa.initVerify(pubKey);
    ecdsa.update(message);
    return ecdsa.verify(signature);
  }

  public static KeyData generateKey() throws Exception {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

    keyGen.initialize(256, random); // 256 bit key size

    KeyPair pair = keyGen.generateKeyPair();
    PrivateKey priv = pair.getPrivate();
    ECPrivateKey ePriv = (ECPrivateKey) priv;
    PublicKey pub = pair.getPublic();

    String encodedPrivateKey = Base64.getEncoder().encodeToString(priv.getEncoded());
    byte[] pubEncoded = pub.getEncoded();
    String encodedPublicKey = Base64.getEncoder().encodeToString(pubEncoded);
    KeyData KD = new KeyData();

    KD.setPrivateKey(encodedPrivateKey);
    KD.setPublicKey(encodedPublicKey);

    return KD;
  }

  public static PrivateKey base64ToPrivateKey(String encodedKey) throws Exception {
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    return bytesToPrivateKey(decodedKey);
  }

  public static PrivateKey bytesToPrivateKey(byte[] pkcs8key) throws GeneralSecurityException {
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8key);
    KeyFactory factory = KeyFactory.getInstance("EC");
    PrivateKey privateKey = factory.generatePrivate(spec);
    return privateKey;
  }

  public static PublicKey base64ToPublicKey(String encodedKey) throws Exception {
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    return bytesToPublicKey(decodedKey);
  }

  public static String getPubHex(PrivateKey privateKey) {

    String pubhex = "";

    try {
      ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
      ECPublicKey ecPublicKey = publicFromPrivate(ecPrivateKey);

      ECPoint ecPoint = ecPublicKey.getW();

      byte[] affineXBytes = ecPoint.getAffineX().toByteArray();
      byte[] affineYBytes = ecPoint.getAffineY().toByteArray();

      String hexX = bytesToHex(affineXBytes);
      String hexY = bytesToHex(affineYBytes);

      pubhex = hexX + ":" + hexY;

    } catch (Exception e) {
      log.error("ERR XXX:" + e);
    }

    return pubhex;
  }

  public static PublicKey getPublicKey(PrivateKey privateKey) {

    PublicKey publicKey = null;

    try {
      ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
      ECPublicKey ecPublicKey = publicFromPrivate(ecPrivateKey);

      publicKey = ecPublicKey;
    } catch (Exception e) {
      log.error("ERR XXX:" + e);
    }

    return publicKey;
  }

  public static ECPublicKey publicFromPrivate(ECPrivateKey privateKey) throws Exception {

    Security.addProvider(new BouncyCastleProvider());

    ECParameterSpec params = privateKey.getParams();
    org.bouncycastle.jce.spec.ECParameterSpec bcSpec =
        org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util.convertSpec(params, false);
    org.bouncycastle.math.ec.ECPoint q = bcSpec.getG().multiply(privateKey.getS());
    org.bouncycastle.math.ec.ECPoint bcW = bcSpec.getCurve().decodePoint(q.getEncoded(false));
    ECPoint w =
        new ECPoint(bcW.getAffineXCoord().toBigInteger(), bcW.getAffineYCoord().toBigInteger());
    ECPublicKeySpec keySpec = new ECPublicKeySpec(w, tryFindNamedCurveSpec(params));
    return (ECPublicKey)
        KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME).generatePublic(keySpec);
  }

  @SuppressWarnings("unchecked")
  public static ECParameterSpec tryFindNamedCurveSpec(ECParameterSpec params) {
    org.bouncycastle.jce.spec.ECParameterSpec bcSpec =
        org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util.convertSpec(params, false);
    for (Object name : Collections.list(org.bouncycastle.jce.ECNamedCurveTable.getNames())) {
      org.bouncycastle.jce.spec.ECNamedCurveParameterSpec bcNamedSpec =
          org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec((String) name);
      if (bcNamedSpec.getN().equals(bcSpec.getN())
          && bcNamedSpec.getH().equals(bcSpec.getH())
          && bcNamedSpec.getCurve().equals(bcSpec.getCurve())
          && bcNamedSpec.getG().equals(bcSpec.getG())) {
        return new org.bouncycastle.jce.spec.ECNamedCurveSpec(
            bcNamedSpec.getName(),
            bcNamedSpec.getCurve(),
            bcNamedSpec.getG(),
            bcNamedSpec.getN(),
            bcNamedSpec.getH(),
            bcNamedSpec.getSeed());
      }
    }
    return params;
  }

  // https://stackoverflow.com/questions/40552688/generating-a-ecdsa-private-key-in-bouncy-castle-returns-a-public-key
  private static String getPrivateKeyAsHex(PrivateKey privateKey) {
    ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
    byte[] privateKeyBytes = ecPrivateKey.getS().toByteArray();

    String hex = bytesToHex(privateKeyBytes);
    return hex;
  }

  private static String getPublicKeyAsHex(PublicKey publicKey) {
    ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
    ECPoint ecPoint = ecPublicKey.getW();

    byte[] affineXBytes = ecPoint.getAffineX().toByteArray();
    byte[] affineYBytes = ecPoint.getAffineY().toByteArray();

    String hexX = bytesToHex(affineXBytes);
    String hexY = bytesToHex(affineYBytes);

    return hexX + ":" + hexY;
  }

  private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static PublicKey bytesToPublicKey(byte[] x509key) throws GeneralSecurityException {
    X509EncodedKeySpec spec = new X509EncodedKeySpec(x509key);
    KeyFactory factory = KeyFactory.getInstance("EC");
    ECPublicKey publicKey = (ECPublicKey) factory.generatePublic(spec);
    // We should be able to use these X and Y in Go to build the public key
    BigInteger x = publicKey.getW().getAffineX();
    BigInteger y = publicKey.getW().getAffineY();
    return publicKey;
  }

  public static String getWallet(String encodedKey) throws GeneralSecurityException {

    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

    X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
    KeyFactory factory = KeyFactory.getInstance("EC");
    ECPublicKey publicKey = (ECPublicKey) factory.generatePublic(spec);

    BigInteger x = publicKey.getW().getAffineX();
    BigInteger y = publicKey.getW().getAffineY();

    String walletWaddress = Base58CheckEncoding.convertToBase58(y.toString(), 10);

    return walletWaddress;
  }

  public static BigInteger extractR(byte[] signature) throws Exception {
    int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
    int lengthR = signature[startR + 1];
    return new BigInteger(Arrays.copyOfRange(signature, startR + 2, startR + 2 + lengthR));
  }

  public static BigInteger extractS(byte[] signature) throws Exception {
    int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
    int lengthR = signature[startR + 1];
    int startS = startR + 2 + lengthR;
    int lengthS = signature[startS + 1];
    return new BigInteger(Arrays.copyOfRange(signature, startS + 2, startS + 2 + lengthS));
  }

  public static byte[] signMsg(String msg, PrivateKey priv) throws Exception {
    Signature ecdsa = Signature.getInstance("SHA1withECDSA");

    ecdsa.initSign(priv);
    byte[] strByte = msg.getBytes("UTF-8");
    ecdsa.update(strByte);
    byte[] realSig = ecdsa.sign();

    return realSig;
  }
}
