/*
use aes_gcm::{
  aead::{Aead, KeyInit},
  Aes128Gcm, Key, Nonce,
};
use base64::{engine::general_purpose, Engine};
use pbkdf2::pbkdf2_hmac;
use rand::{rngs::OsRng, RngCore};
use sha2::Sha256;
use zeroize::{Zeroize, Zeroizing};

use std::sync::{Arc, Mutex};
*/

/*fn derive_secret_key(password: &str, salt: &str) -> Result<Vec<u8>, String> {
  let mut key = vec![0u8; 16];
  pbkdf2_hmac::<Sha256>(password.as_bytes(), salt.as_bytes(), 600_000, &mut key);
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




pub struct VaultState {
  pub key: Arc<Mutex<Option<Zeroizing<[u8; 16]>>>>
}



// Commande d'ouverture
#[tauri::command]
pub fn unlock_vault(
  password: String,
  salt: String,
  encrypted_master_key: String,
  state: tauri::State<'_, VaultState>
) -> Result<String, String> {

  let mut  secret_key = derive_secret_key(&password, &salt)?;

  //let mut lock = state.key.lock().unwrap();

  let master_key_vec = decrypt_master_key(&encrypted_master_key, &secret_key)?;

  secret_key.zeroize();


  let mut key_array = [0u8; 16];
  key_array.copy_from_slice(&master_key_vec[..16]);


  let mut lock = state.key.lock().unwrap();
  *lock = Some(Zeroizing::new(key_array));

  Ok("unlocked".to_string())
}



#[tauri::command]
pub fn read_file(
  chemin: String,
  iv : String,
  state: tauri::State<'_, VaultState>
) -> Result<Vec<u8>, String> {


  let key_copy: [u8; 16] = {
    let lock = state.key.lock().unwrap();
    let key = lock.as_ref().ok_or("Coffre verrouillé")?;
    **key
  };

  use base64::{Engine as _, engine::general_purpose};
  let iv = general_purpose::STANDARD
    .decode(&iv)
    .map_err(|e| format!("IV invalide : {}", e))?;

  let encrypted_bytes = std::fs::read(&chemin)
    .map_err(|e| format!("Lecture fichier échouée : {}", e))?;

  decrypt_with_master_key(&encrypted_bytes, &key_copy)
}

#[tauri::command]
pub fn lock_vault(state: tauri::State<'_, VaultState>) -> Result<(), String> {
  let mut lock = state.key.lock().unwrap();
  *lock = None;
  Ok(())
}


 */
