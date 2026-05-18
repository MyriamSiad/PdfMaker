// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]
mod encrypt;

fn main() {


     tauri::Builder::default()
           .invoke_handler(tauri::generate_handler![
             encrypt::encrypt_pdf,
              encrypt::decrypt_pdf,
           ])
           .run(tauri::generate_context!())
          .expect("Erreur Tauri");
      app_lib::run();

}
