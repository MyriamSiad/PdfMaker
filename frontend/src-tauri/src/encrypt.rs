
use aes_gcm::{
  aead::{Aead, KeyInit},
  Aes128Gcm, Key, Nonce,
};
use base64::{engine::general_purpose, Engine};
use pbkdf2::pbkdf2_hmac;
use rand::{rngs::OsRng, RngCore};
use sha2::Sha256;
use zeroize::Zeroize;


fn derive_secret_key(password: &str, salt: &str) -> Result<Vec<u8>, String> {
  let mut key = vec![0u8; 16];
  pbkdf2_hmac::<Sha256>(
    password.as_bytes(),
    salt.as_bytes(),
    65536,
    &mut key,
  );
  Ok(key)
}


fn decrypt_master_key(encrypted_b64: &str, secret_key: &[u8]) -> Result<Vec<u8>, String> {
  let iv_plus_cipher = general_purpose::STANDARD
    .decode(encrypted_b64)
    .map_err(|e| format!("Erreur décodage Base64 : {}", e))?;

  if iv_plus_cipher.len() < 12 {
    return Err("Données MasterKey invalides".to_string());
  }

  let iv = &iv_plus_cipher[..12];
  let encrypted = &iv_plus_cipher[12..];

  let key = Key::<Aes128Gcm>::from_slice(secret_key);
  let cipher = Aes128Gcm::new(key);
  let nonce = Nonce::from_slice(iv);

  // Si le mot de passe est mauvais, AES-GCM échoue ici → "Mot de passe incorrect".
  cipher
    .decrypt(nonce, encrypted)
    .map_err(|_| "Mot de passe incorrect".to_string())
}

fn encrypt_with_master_key(data: &[u8], master_key: &[u8]) -> Result<Vec<u8>, String> {
  let mut iv = [0u8; 12];
  OsRng.fill_bytes(&mut iv);

  let key = Key::<Aes128Gcm>::from_slice(master_key);
  let cipher = Aes128Gcm::new(key);
  let nonce = Nonce::from_slice(&iv);

  println!("=== IV CHIFFREMENT : {:?}", &iv);
  let encrypted = cipher
    .encrypt(nonce, data)
    .map_err(|e| format!("Erreur chiffrement PDF : {}", e))?;

  let mut result = Vec::with_capacity(12 + encrypted.len());
  result.extend_from_slice(&iv);
  result.extend_from_slice(&encrypted);

  Ok(result)
}



fn decrypt_with_master_key(data: &[u8], master_key: &[u8]) -> Result<Vec<u8>, String> {
  if data.len() < 12 {
    return Err("Fichier chiffré invalide".to_string());
  }

  let iv = &data[..12];
  let encrypted = &data[12..];

  let key = Key::<Aes128Gcm>::from_slice(master_key);
  let cipher = Aes128Gcm::new(key);
  let nonce = Nonce::from_slice(iv);
  println!("=== IV DÉCHIFFREMENT : {:?}", &data[..12]);
  cipher
    .decrypt(nonce, encrypted)
    .map_err(|_| "Déchiffrement PDF échoué, fichier corrompu ou clé incorrecte".to_string())
}


/*#[tauri::command]
pub fn encrypt_pdf(
  password: String,
  salt: String,
  encrypted_master_key: String,
  pdf_bytes: Vec<u8>,
) -> Result<Vec<u8>, String> {
  // Dériver la Clé B
  let mut secret_key = derive_secret_key(&password, &salt)?;

  // Déchiffrer la MasterKey — si le mot de passe est faux, on s'arrête ici
  let mut master_key = decrypt_master_key(&encrypted_master_key, &secret_key)?;

  // Chiffrer le PDF
  let result = encrypt_with_master_key(&pdf_bytes, &master_key);

  // Effacer les secrets de la RAM immédiatement (Rust garantit l'exécution ici)
  secret_key.zeroize();
  master_key.zeroize();

  result
}*/

/*#[tauri::command]
pub fn decrypt_pdf(
  password: String,
  salt: String,
  encrypted_master_key: String,
  encrypted_pdf: Vec<u8>,
) -> Result<Vec<u8>, String> {
  let mut secret_key = derive_secret_key(&password, &salt)?;
  let mut master_key = decrypt_master_key(&encrypted_master_key, &secret_key)?;

  println!("=== encrypted_pdf length : {}", encrypted_pdf.len());
  println!("=== secret_key length : {}", secret_key.len());
  println!("=== master_key length : {}", master_key.len());
  println!("=== IV extrait : {:?}", &encrypted_pdf[..12]);

  let result = decrypt_with_master_key(&encrypted_pdf, &master_key);

  secret_key.zeroize();
  master_key.zeroize();

  result
}*/


#[tauri::command]
pub fn encrypt_pdf(
  password: String,
  salt: String,
  encrypted_master_key: String,
  pdf_bytes_b64: String,
) -> Result<String, String> {
  let pdf_bytes = general_purpose::STANDARD
    .decode(&pdf_bytes_b64)
    .map_err(|e| format!("Erreur décodage base64 : {}", e))?;

  let mut secret_key = derive_secret_key(&password, &salt)?;
  let mut master_key = decrypt_master_key(&encrypted_master_key, &secret_key)?;
  let result = encrypt_with_master_key(&pdf_bytes, &master_key);

  secret_key.zeroize();
  master_key.zeroize();

  result.map(|bytes| general_purpose::STANDARD.encode(&bytes))
}

#[tauri::command]
pub fn decrypt_pdf(
  password: String,
  salt: String,
  encrypted_master_key: String,
  encrypted_pdf_b64: String,
) -> Result<String, String> {
  let encrypted_pdf = general_purpose::STANDARD
    .decode(&encrypted_pdf_b64)
    .map_err(|e| format!("Erreur décodage base64 : {}", e))?;

  let mut secret_key = derive_secret_key(&password, &salt)?;
  let mut master_key = decrypt_master_key(&encrypted_master_key, &secret_key)?;
  let result = decrypt_with_master_key(&encrypted_pdf, &master_key);

  secret_key.zeroize();
  master_key.zeroize();

  result.map(|bytes| general_purpose::STANDARD.encode(&bytes))
}
