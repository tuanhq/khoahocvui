package com.ligerdev.appbase.utils.encrypt;
 

public class RSAkeyObj {
	
private String privateKey;
private String publicKey;

   public RSAkeyObj(String privateKey, String publicKey) {
      this.privateKey = privateKey;
      this.publicKey = publicKey;
   }

   public RSAkeyObj() {
   }

   public String getPrivateKey() {
      return privateKey;
   }

   public String getPublicKey() {
      return publicKey;
   }

   public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
   }

   public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
   }
}
