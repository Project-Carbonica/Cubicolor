import { MessageTheme } from '../types/theme';

interface RGB {
  r: number;
  g: number;
  b: number;
}

interface HSL {
  h: number;
  s: number;
  l: number;
}

// Convert hex to RGB
function hexToRgb(hex: string): RGB {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result
    ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16),
      }
    : { r: 0, g: 0, b: 0 };
}

// Convert RGB to hex
function rgbToHex(r: number, g: number, b: number): string {
  return '#' + [r, g, b].map(x => {
    const hex = Math.round(Math.max(0, Math.min(255, x))).toString(16);
    return hex.length === 1 ? '0' + hex : hex;
  }).join('');
}

// Convert RGB to HSL
function rgbToHsl(r: number, g: number, b: number): HSL {
  r /= 255;
  g /= 255;
  b /= 255;

  const max = Math.max(r, g, b);
  const min = Math.min(r, g, b);
  let h = 0;
  let s = 0;
  const l = (max + min) / 2;

  if (max !== min) {
    const d = max - min;
    s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

    switch (max) {
      case r:
        h = ((g - b) / d + (g < b ? 6 : 0)) / 6;
        break;
      case g:
        h = ((b - r) / d + 2) / 6;
        break;
      case b:
        h = ((r - g) / d + 4) / 6;
        break;
    }
  }

  return { h: h * 360, s: s * 100, l: l * 100 };
}

// Convert HSL to RGB
function hslToRgb(h: number, s: number, l: number): RGB {
  h /= 360;
  s /= 100;
  l /= 100;

  let r, g, b;

  if (s === 0) {
    r = g = b = l;
  } else {
    const hue2rgb = (p: number, q: number, t: number) => {
      if (t < 0) t += 1;
      if (t > 1) t -= 1;
      if (t < 1 / 6) return p + (q - p) * 6 * t;
      if (t < 1 / 2) return q;
      if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
      return p;
    };

    const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
    const p = 2 * l - q;

    r = hue2rgb(p, q, h + 1 / 3);
    g = hue2rgb(p, q, h);
    b = hue2rgb(p, q, h - 1 / 3);
  }

  return { r: r * 255, g: g * 255, b: b * 255 };
}

// Adjust color lightness
function adjustLightness(hex: string, lightnessChange: number): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);
  hsl.l = Math.max(0, Math.min(100, hsl.l + lightnessChange));
  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

// Adjust color saturation
function adjustSaturation(hex: string, saturationChange: number): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);
  hsl.s = Math.max(0, Math.min(100, hsl.s + saturationChange));
  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

// Shift hue
function shiftHue(hex: string, hueShift: number): string {
  const rgb = hexToRgb(hex);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);
  hsl.h = (hsl.h + hueShift + 360) % 360;
  const newRgb = hslToRgb(hsl.h, hsl.s, hsl.l);
  return rgbToHex(newRgb.r, newRgb.g, newRgb.b);
}

/**
 * Auto-generate a complete theme from a single base color
 */
export function generateTheme(baseColor: string, themeName: string = 'Generated Theme'): MessageTheme {
  const rgb = hexToRgb(baseColor);
  const hsl = rgbToHsl(rgb.r, rgb.g, rgb.b);

  // Determine if we're in dark or light mode based on lightness
  const isDark = hsl.l < 50;

  const messages: MessageTheme['messages'] = {
    // Error - Red hue
    ERROR: {
      color: isDark ? '#FF5555' : '#B00020',
      decorations: ['BOLD'],
    },
    // Success - Green hue
    SUCCESS: {
      color: isDark ? '#55FF55' : '#2E7D32',
      decorations: ['BOLD'],
    },
    // Warning - Yellow/Orange hue
    WARNING: {
      color: isDark ? '#FFAA00' : '#F57C00',
      decorations: ['BOLD'],
    },
    // Info - Blue hue
    INFO: {
      color: isDark ? '#55FFFF' : '#1976D2',
      decorations: [],
    },
    // Highlight - Based on base color, brighter
    HIGHLIGHT: {
      color: adjustLightness(adjustSaturation(baseColor, 20), isDark ? 20 : -10),
      decorations: ['BOLD'],
    },
    // Primary - Base color
    PRIMARY: {
      color: baseColor,
      decorations: ['BOLD'],
    },
    // Secondary - Complementary color (hue shift)
    SECONDARY: {
      color: shiftHue(baseColor, 180),
      decorations: [],
    },
    // Accent - Analogous color
    ACCENT: {
      color: shiftHue(baseColor, 30),
      decorations: [],
    },
    // Title - High contrast
    TITLE: {
      color: isDark ? '#FFFFFF' : '#000000',
      decorations: ['BOLD', 'UNDERLINED'],
    },
    // Subtitle - Medium emphasis
    SUBTITLE: {
      color: isDark ? '#E0E0E0' : '#212121',
      decorations: ['BOLD'],
    },
    // Body - Normal text
    BODY: {
      color: isDark ? '#FFFFFF' : '#000000',
      decorations: [],
    },
    // Label - Lower emphasis
    LABEL: {
      color: isDark ? '#B0B0B0' : '#616161',
      decorations: [],
    },
    // Muted - Very low emphasis
    MUTED: {
      color: isDark ? '#757575' : '#9E9E9E',
      decorations: ['ITALIC'],
    },
    // Link - Blue, underlined
    LINK: {
      color: isDark ? '#55FFFF' : '#1976D2',
      decorations: ['UNDERLINED'],
    },
    // Disabled - Gray, strikethrough
    DISABLED: {
      color: isDark ? '#555555' : '#BDBDBD',
      decorations: ['STRIKETHROUGH'],
    },
  };

  return {
    name: themeName,
    messages,
  };
}

/**
 * Get preset themes
 */
export function getPresetThemes(): Record<string, MessageTheme> {
  return {
    'minecraft-dark': {
      name: 'Minecraft Dark',
      messages: {
        ERROR: { color: '#FF5555', decorations: ['BOLD'] },
        SUCCESS: { color: '#55FF55', decorations: ['BOLD'] },
        WARNING: { color: '#FFFF55', decorations: ['BOLD'] },
        INFO: { color: '#55FFFF', decorations: [] },
        HIGHLIGHT: { color: '#FFAA00', decorations: ['BOLD'] },
        PRIMARY: { color: '#5555FF', decorations: ['BOLD'] },
        SECONDARY: { color: '#AA00AA', decorations: [] },
        MUTED: { color: '#AAAAAA', decorations: [] },
        TITLE: { color: '#FFFF55', decorations: ['BOLD'] },
        SUBTITLE: { color: '#FFFFFF', decorations: ['BOLD'] },
        BODY: { color: '#FFFFFF', decorations: [] },
        LABEL: { color: '#AAAAAA', decorations: [] },
        ACCENT: { color: '#FF55FF', decorations: [] },
        LINK: { color: '#55FFFF', decorations: ['UNDERLINED'] },
        DISABLED: { color: '#555555', decorations: [] },
      },
    },
    'minecraft-light': {
      name: 'Minecraft Light',
      messages: {
        ERROR: { color: '#B00020', decorations: ['BOLD'] },
        SUCCESS: { color: '#2E7D32', decorations: ['BOLD'] },
        WARNING: { color: '#F57C00', decorations: ['BOLD'] },
        INFO: { color: '#1976D2', decorations: [] },
        HIGHLIGHT: { color: '#C51162', decorations: ['BOLD'] },
        PRIMARY: { color: '#1565C0', decorations: ['BOLD'] },
        SECONDARY: { color: '#6A1B9A', decorations: [] },
        MUTED: { color: '#757575', decorations: [] },
        TITLE: { color: '#000000', decorations: ['BOLD'] },
        SUBTITLE: { color: '#424242', decorations: ['BOLD'] },
        BODY: { color: '#212121', decorations: [] },
        LABEL: { color: '#616161', decorations: [] },
        ACCENT: { color: '#D81B60', decorations: [] },
        LINK: { color: '#1976D2', decorations: ['UNDERLINED'] },
        DISABLED: { color: '#BDBDBD', decorations: [] },
      },
    },
    'material-dark': generateTheme('#BB86FC', 'Material Dark'),
    'material-light': generateTheme('#6750A4', 'Material Light'),
  };
}