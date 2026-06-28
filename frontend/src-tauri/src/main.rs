// Prevents additional console window on Windows in release, DO NOT REMOVE!!
#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]

use std::process::{Child, Command};
use std::sync::Mutex;
use tauri::Manager;


struct BackendProcess(Mutex<Option<Child>>);
fn check_java() -> Result<String, String> {
  let output = Command::new("java")
    .arg("-version")
    .output();

  match output {
    Ok(out) => {

      let version = String::from_utf8_lossy(&out.stderr).to_string();

      if version.contains("21") || version.contains("22") || version.contains("23") {
        Ok(version)
      } else {
        Err(format!(
          "Java 21 minimum requis.\nVersion détectée : {}",
          version
        ))
      }
    }
    Err(_) => Err(
      "Java n'est pas installé sur votre machine.\n\nVeuillez installer Java 21 depuis :\nhttps://adoptium.net".to_string()
    ),
  }
}
fn start_backend(app: &tauri::AppHandle) -> Option<Child> {
  let resource_path = app
    .path()
    .resolve("resources/pdf-maker.jar", tauri::path::BaseDirectory::Resource)
    .expect("Impossible de trouver le JAR");

  let path_str = resource_path
    .to_str()
    .unwrap()
    .trim_start_matches("\\\\?\\");  // ← supprimer le préfixe UNC

  let child = Command::new("java")
    .args(["-jar", path_str])
    .spawn();
  match child {
    Ok(process) => {
      println!("Backend démarré avec le PID : {}", process.id());
      Some(process)
    }
    Err(e) => {
      eprintln!("Erreur au démarrage du backend : {}", e);
      None
    }
  }
}
fn main() {

  tauri::Builder::default()
    .manage(BackendProcess(Mutex::new(None)))
    .setup(|app| {
      let handle = app.handle();
      let process = start_backend(&handle);

      // Stocker le process pour le tuer à la fermeture
      *app.state::<BackendProcess>().0.lock().unwrap() = process;

      // Attendre que le backend soit prêt
      std::thread::sleep(std::time::Duration::from_secs(3));

      Ok(())
    })
    .on_window_event(|window, event| {          // ← v2 : signature différente
      if let tauri::WindowEvent::Destroyed = event {
        let state = window.state::<BackendProcess>();
        if let Some(mut child) = state.0.lock().unwrap().take() {
          child.kill().expect("Impossible d'arrêter le backend");
          println!("Backend arrêté proprement");
        };
      }
    })
    .run(tauri::generate_context!())
    .expect("Erreur lors du démarrage de Tauri");
}
