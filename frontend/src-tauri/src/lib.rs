#[cfg_attr(mobile, tauri::mobile_entry_point)]

//mod encrypt;
pub fn run() {
  tauri::Builder::default()
    .invoke_handler(tauri::generate_handler![
             //encrypt::encrypt_pdf,
              //encrypt::decrypt_pdf,
             //encrypt::unlock_vault,
             //encrypt::read_file,
             //encrypt::lock_vault,
           ])
    .plugin(tauri_plugin_dialog::init())
    .plugin(tauri_plugin_fs::init())
    .run(tauri::generate_context!())
    .expect("error while running tauri application");



}
