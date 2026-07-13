import "@testing-library/jest-dom/vitest";
import { vi } from "vitest";

const storage = new Map();

class ResizeObserverMock {
  observe() {}
  unobserve() {}
  disconnect() {}
}

Object.defineProperty(window, "matchMedia", {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn()
  }))
});

Object.defineProperty(window, "ResizeObserver", {
  writable: true,
  value: ResizeObserverMock
});

const originalGetComputedStyle = window.getComputedStyle;
Object.defineProperty(window, "getComputedStyle", {
  writable: true,
  value: (element) => originalGetComputedStyle(element)
});

Object.defineProperty(window, "localStorage", {
  writable: true,
  value: {
    getItem: vi.fn((key) => storage.get(key) ?? null),
    setItem: vi.fn((key, value) => {
      storage.set(key, String(value));
    }),
    removeItem: vi.fn((key) => {
      storage.delete(key);
    }),
    clear: vi.fn(() => {
      storage.clear();
    })
  }
});

Object.defineProperty(window, "scrollTo", {
  writable: true,
  value: vi.fn()
});

Object.defineProperty(window, "speechSynthesis", {
  writable: true,
  value: {
    speak: vi.fn(),
    cancel: vi.fn()
  }
});

globalThis.SpeechSynthesisUtterance = class SpeechSynthesisUtterance {
  constructor(text) {
    this.text = text;
    this.onend = null;
    this.onerror = null;
  }
};

globalThis.Audio = class AudioMock {
  constructor() {
    this.onended = null;
    this.onerror = null;
  }

  play() {
    return Promise.resolve();
  }

  pause() {}
};
