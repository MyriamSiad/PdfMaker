// ============================================================
//  theme.service.ts — Gestion Light / Dark mode
//  App PDF Desktop · Tauri + Angular
//
//  Usage :
//    constructor(private theme: ThemeService) {}
//    this.theme.toggle();
//    this.theme.setTheme('dark');
//    this.theme.isDark$  // Observable<boolean>
// ============================================================

import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {

  private readonly STORAGE_KEY = 'app-theme';
  private readonly renderer: Renderer2;
  private readonly themeSubject: BehaviorSubject<Theme>;

  readonly isDark$: Observable<boolean>;
  readonly theme$: Observable<Theme>;

  constructor(factory: RendererFactory2) {
    this.renderer = factory.createRenderer(null, null);

    const saved   = localStorage.getItem(this.STORAGE_KEY) as Theme | null;
    const initial = saved ?? this.detectSystemTheme();

    this.themeSubject = new BehaviorSubject<Theme>(initial);
    this.theme$       = this.themeSubject.asObservable();
    this.isDark$      = new BehaviorSubject<boolean>(initial === 'dark');

    // Synchronise l'Observable isDark$ avec theme$
    this.theme$.subscribe(t =>
      (this.isDark$ as BehaviorSubject<boolean>).next(t === 'dark')
    );

    this.applyTheme(initial);
  }

  /** Retourne le thème actif */
  get current(): Theme {
    return this.themeSubject.value;
  }

  get isDark(): boolean {
    return this.current === 'dark';
  }

  /** Bascule entre light et dark */
  toggle(): void {
    this.setTheme(this.isDark ? 'light' : 'dark');
  }

  /** Définit un thème précis */
  setTheme(theme: Theme): void {
    this.applyTheme(theme);
    this.themeSubject.next(theme);
    localStorage.setItem(this.STORAGE_KEY, theme);
  }

  /** Suit la préférence système */
  followSystem(): void {
    const theme = this.detectSystemTheme();
    this.setTheme(theme);

    window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', e => {
        this.setTheme(e.matches ? 'dark' : 'light');
      });
  }

  // ── Privé ──────────────────────────────────────────────────

  private applyTheme(theme: Theme): void {
    this.renderer.setAttribute(
      document.documentElement,
      'data-theme',
      theme
    );
  }

  private detectSystemTheme(): Theme {
    return window.matchMedia('(prefers-color-scheme: dark)').matches
      ? 'dark'
      : 'light';
  }
}
