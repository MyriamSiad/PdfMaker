import {Injectable} from '@angular/core';


const API_URL = "http://localhost:8080/api/errors";

export interface FrontendError {
  exceptionClass: string;
  message: string;
  stackTrace: string;
  uri: string;
  origin: "FRONTEND";
}

export async function logFrontendError(error: Error, uri?: string): Promise<void> {
  const user = sessionStorage.getItem('user');
  const token = user ? JSON.parse(user).accessToken : null;

  const payload: FrontendError = {
    exceptionClass: error.name,
    message: error.message,
    stackTrace: error.stack ?? "",
    uri: uri ?? window.location.pathname,
    origin: "FRONTEND",
  };

  try {
    await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json",
      'Authorization': `Bearer ${token}`},
      body: JSON.stringify(payload),
    });
  } catch (e) {
    console.error("Impossible de logger l'erreur en base :", e);
  }
}
